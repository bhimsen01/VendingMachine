import java.io.*;
import java.nio.file.*;

public class Membership {
    private String memberPIN;

    public void loadMemberPIN() {
        try {
            memberPIN = Files.readString(Path.of("member_pin.txt")).trim();
        } catch (IOException e) {
            System.out.println("Error loading membership PIN. Membership features disabled.");
            memberPIN = null;
        }
    }

    public boolean verifyMember(String inputPIN) {
        return memberPIN != null && memberPIN.equals(inputPIN);
    }
}

