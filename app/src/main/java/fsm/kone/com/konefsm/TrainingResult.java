package fsm.kone.com.konefsm;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 4/11/17.
 */

public class TrainingResult {

    public String uid;
    public Long timestamp;
    public String username;
    public String email;
    public String productName;
    public String role;
    public Double latitude;
    public Double longitude;

    public TrainingResult() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("username", username);
        result.put("email", email);
        result.put("productName", productName);
        result.put("role", role);
        result.put("timestamp", timestamp);
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }
}
