package loginpage.src;

import java.util.*;
public class ProfileManager {
    ArrayList<Profile> profiles;
    ProfileManager() {
        profiles = new ArrayList<Profile>();
    }
    public int add(String name, String pass) {
        if(searchProfile(name)==-1) {
            profiles.add(new Profile(name, pass));
            return 0;
        }
        return -1;
    }
    public int searchProfile(String username) {
        for(int i = 0; i < profiles.size(); i ++) {
            if(profiles.get(i).username.equals(username)) {
                return i;
            }
        }
        return -1;
    }
    public void display() {
        Profile cur;
        for(int i = 0; i < profiles.size(); i ++) {
            cur = profiles.get(i);
            System.out.println((i+1)+".\tUsername :" + cur.username);
            System.out.println("\tPassword :" + cur.password);
        }
    }
}
class Profile {
    String username;
    String password;
    Profile(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // public void changeUN();
    // public void changeP();
    // public int logIn(String username, String password);
}