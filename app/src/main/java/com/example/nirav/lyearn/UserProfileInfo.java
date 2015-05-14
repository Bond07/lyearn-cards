package com.example.nirav.lyearn;

/**
 * Created by nirav on 5/4/15.
 */
public class UserProfileInfo {

    private static String userProfileName;
    private static String userRole;
    private static String userSkypeId;
    private static String userContactNumber;
    private static String userProfilePhotoURl;
    private static String userEmailId;
    private static String userPoints;
    private static String userID;



    public static class UserInfo {

        public void setInfo(String firstName, String lastName, String whatIDo, String skypeId, String phoneNumber, String imageUrl, String emailId, String points, String id) {
            userProfileName = firstName + " " + lastName;
            userRole = whatIDo;
            userSkypeId = skypeId;
            userContactNumber = phoneNumber;
            userProfilePhotoURl = imageUrl;
            userEmailId = emailId;
            userPoints = points;
            userID = id;

        }

        public String getUserProfileName() {
            return userProfileName;
        }

        public void setUserProfileName(String name) {
            userProfileName = name;
        }


        public String getUserRole() {
            return userRole;
        }

        public void setUserRole(String role) {
            userRole = role;
        }


        public String getUserSkypeId() {
            return userSkypeId;
        }

        public void setUserSkypeId(String skypeId) {
            userSkypeId = skypeId;
        }


        public String getUserContactNumber() {
            return userContactNumber;
        }

        public void setUserContactNumber(String contactNumber) {
            userContactNumber = contactNumber;
        }


        public String getUserProfilePhotoURl() {
            return userProfilePhotoURl;
        }

        public void setUserProfilePhotoURL(String photoURL) {
            userProfilePhotoURl = photoURL;
        }


        public String getUserEmailId() {
            return userEmailId;
        }

        public void setUserEmailId(String emailId) {
            userEmailId = emailId;
        }


        public String getUserPoints() {
            return userPoints;
        }

        public void setUserPoints(String points) {
            userPoints = points;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            UserProfileInfo.userID = userID;
        }


    }


}
