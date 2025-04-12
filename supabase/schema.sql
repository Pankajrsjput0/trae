-- Enable Row Level Security
ALTER DATABASE postgres SET "app.jwt_secret" = 'your-jwt-secret';

-- Create tables
CREATE TABLE users (
    id UUID REFERENCES auth.users NOT NULL PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE app_usage_stats (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id UUID REFERENCES users(id) NOT NULL,
    app_package_name TEXT NOT NULL,
    usage_time_minutes INTEGER DEFAULT 0,
    date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, app_package_name, date)
);

CREATE TABLE restricted_apps (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id UUID REFERENCES users(id) NOT NULL,
    app_package_name TEXT NOT NULL,
    daily_limit_minutes INTEGER DEFAULT 60,
    is_blocked BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, app_package_name)
);

CREATE TABLE coin_transactions (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id UUID REFERENCES users(id) NOT NULL,
    amount INTEGER NOT NULL,
    transaction_type TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_app_usage_stats_user_date ON app_usage_stats(user_id, date);
CREATE INDEX idx_restricted_apps_user ON restricted_apps(user_id);
CREATE INDEX idx_coin_transactions_user ON coin_transactions(user_id);

-- Enable Row Level Security on all tables
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE app_usage_stats ENABLE ROW LEVEL SECURITY;
ALTER TABLE restricted_apps ENABLE ROW LEVEL SECURITY;
ALTER TABLE coin_transactions ENABLE ROW LEVEL SECURITY;

-- Create policies
CREATE POLICY "Users can view their own data" ON users
    FOR SELECT USING (auth.uid() = id);

CREATE POLICY "Users can update their own data" ON users
    FOR UPDATE USING (auth.uid() = id);

CREATE POLICY "Users can view their own app usage stats" ON app_usage_stats
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can insert their own app usage stats" ON app_usage_stats
    FOR INSERT WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can update their own app usage stats" ON app_usage_stats
    FOR UPDATE USING (auth.uid() = user_id);

CREATE POLICY "Users can view their restricted apps" ON restricted_apps
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can manage their restricted apps" ON restricted_apps
    FOR ALL USING (auth.uid() = user_id);

CREATE POLICY "Users can view their coin transactions" ON coin_transactions
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can insert their coin transactions" ON coin_transactions
    FOR INSERT WITH CHECK (auth.uid() = user_id);

-- Create function to update timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updating timestamps
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_app_usage_stats_updated_at
    BEFORE UPDATE ON app_usage_stats
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_restricted_apps_updated_at
    BEFORE UPDATE ON restricted_apps
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();