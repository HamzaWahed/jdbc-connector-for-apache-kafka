/*
 * Copyright 2019 Aiven Oy and jdbc-connector-for-apache-kafka project contributors
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiven.connect.jdbc.util;

/**
 * A summary of the version information about a JDBC driver and the database.
 */
public class JdbcDriverInfo {

    private final int jdbcMajorVersion;
    private final int jdbcMinorVersion;
    private final String jdbcDriverName;
    private final String productName;
    private final String productVersion;

    /**
     * Creates the driver information.
     *
     * @param builder the JDBC builder object
     */
    public JdbcDriverInfo(Builder builder) {
        this.jdbcMajorVersion = builder.jdbcMajorVersion;
        this.jdbcMinorVersion = builder.jdbcMinorVersion;
        this.jdbcDriverName = builder.jdbcDriverName;
        this.productName = builder.productName;
        this.productVersion = builder.productVersion;
    }

    public static class Builder {
        private int jdbcMajorVersion;
        private int jdbcMinorVersion;
        private String jdbcDriverName;
        private String productName;
        private String productVersion;

        public Builder jdbcMajorVersion(int jdbcMajorVersion) {
            this.jdbcMajorVersion = jdbcMajorVersion;
            return this;
        }

        public Builder jdbcMinorVersion(int jdbcMinorVersion) {
            this.jdbcMinorVersion = jdbcMinorVersion;
            return this;
        }

        public Builder jdbcDriverName(String jdbcDriverName) {
            this.jdbcDriverName = jdbcDriverName;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder productVersion(String productVersion) {
            this.productVersion = productVersion;
            return this;
        }

        public int getJdbcMajorVersion() {
            return jdbcMajorVersion;
        }

        public void setJdbcMajorVersion(int jdbcMajorVersion) {
            this.jdbcMajorVersion = jdbcMajorVersion;
        }

        public int getJdbcMinorVersion() {
            return jdbcMinorVersion;
        }

        public void setJdbcMinorVersion(int jdbcMinorVersion) {
            this.jdbcMinorVersion = jdbcMinorVersion;
        }

        public String getJdbcDriverName() {
            return jdbcDriverName;
        }

        public void setJdbcDriverName(String jdbcDriverName) {
            this.jdbcDriverName = jdbcDriverName;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductVersion() {
            return productVersion;
        }

        public void setProductVersion(String productVersion) {
            this.productVersion = productVersion;
        }

        public JdbcDriverInfo build() {
            return new JdbcDriverInfo(this);
        }

    }

    /**
     * Get the major version of the JDBC specification supported by the driver.
     *
     * @return the major version number
     */
    public int jdbcMajorVersion() {
        return jdbcMajorVersion;
    }

    /**
     * Get the minor version of the JDBC specification supported by the driver.
     *
     * @return the minor version number
     */
    public int jdbcMinorVersion() {
        return jdbcMinorVersion;
    }

    /**
     * Get the name of the database product.
     *
     * @return the name of the database product
     */
    public String productName() {
        return productName;
    }

    /**
     * Get the version of the database product.
     *
     * @return the version of the database product
     */
    public String productVersion() {
        return productVersion;
    }

    /**
     * Get the name of the JDBC driver.
     *
     * @return the name of the JDBC driver
     */
    public String jdbcDriverName() {
        return jdbcDriverName;
    }

    /**
     * Determine if the JDBC driver supports at least the specified major and minor version of the
     * JDBC specifications. This can be used to determine whether or not to call JDBC methods.
     *
     * @param jdbcMajorVersion the required major version of the JDBC specification
     * @param jdbcMinorVersion the required minor version of the JDBC specification
     * @return true if the driver supports at least the specified version of the JDBC specification,
     *     or false if the driver supports an older version of the JDBC specification
     */
    public boolean jdbcVersionAtLeast(
        final int jdbcMajorVersion,
        final int jdbcMinorVersion
    ) {
        if (this.jdbcMajorVersion() > jdbcMajorVersion) {
            return true;
        }
        if (jdbcMajorVersion == jdbcMajorVersion() && jdbcMinorVersion() >= jdbcMinorVersion) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (productName() != null) {
            sb.append(productName()).append(' ');
        }
        if (productVersion() != null) {
            sb.append(productVersion()).append(' ');
        }
        if (jdbcDriverName() != null) {
            sb.append(" using ").append(jdbcDriverName()).append(' ');
        }
        sb.append(jdbcMajorVersion()).append('.').append(jdbcMinorVersion());
        return sb.toString();
    }
}
