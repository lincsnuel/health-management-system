-- 1. Tables
CREATE TABLE IF NOT EXISTS "users" (
                                       "id" VARCHAR(255) PRIMARY KEY,
                                       "email" VARCHAR(255),
                                       "phone_number" VARCHAR(50),
                                       "password_hash" VARCHAR(255),
                                       "tenant_id" VARCHAR(255) NOT NULL,
                                       "user_type" VARCHAR(50) NOT NULL,
                                       "is_enabled" BOOLEAN DEFAULT TRUE,
                                       "created_at" TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "refresh_tokens" (
                                                "id" VARCHAR(255) PRIMARY KEY, -- JTI from JWT
                                                "user_id" VARCHAR(255) NOT NULL,
                                                "tenant_id" VARCHAR(255) NOT NULL,
                                                "token" TEXT NOT NULL,         -- Maps to tokenHash in entity

    -- New Metadata Fields from Entity
                                                "device_id" VARCHAR(255),
                                                "device_name" VARCHAR(255),
                                                "ip_address" VARCHAR(45),      -- Supports IPv6 lengths
                                                "user_agent" TEXT,
                                                "last_used_at" TIMESTAMP WITH TIME ZONE,

                                                "expiry_date" TIMESTAMP WITH TIME ZONE NOT NULL,
                                                "created_date" TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                                CONSTRAINT fk_user FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE
);

-- 2. Indexes
CREATE INDEX IF NOT EXISTS idx_users_email ON "users" ("email");
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id ON "refresh_tokens" ("user_id");
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_token ON "refresh_tokens" ("token");
-- Added index for device-specific lookups if needed
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_device_id ON "refresh_tokens" ("device_id");