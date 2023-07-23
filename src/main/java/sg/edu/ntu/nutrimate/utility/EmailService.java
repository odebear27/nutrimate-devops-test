package sg.edu.ntu.nutrimate.utility;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
