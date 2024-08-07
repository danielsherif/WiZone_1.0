    package org.example.wizone.model;

    public class User {
        private String username;
        private String passwordHash;
        private String firstName;
        private String lastName;

        public User(String username, String passwordHash, String firstName, String lastName) {
            this.username = username;
            this.passwordHash = passwordHash;
            this.firstName = firstName;
            this.lastName = lastName;
        }



        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPasswordHash() {
            return passwordHash;
        }

        public void setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
        }
    }