package io.reist.sandbox.cryptocurrency.model.local;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Sergey on 02/11/2017.
 */
public final class CryptoCurrencyList {

    @SerializedName("BaseImageUrl")
    public String baseUrl;

    @SerializedName("Data")
    public Map<String, Details> data;

    public class Details {

        @SerializedName("ImageUrl")
        public String image;

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        int counter = 0;

        for (String str : data.keySet()) {
            sb.append(str).append(",");
            counter++;
            if (counter == 10) break;
        }

        int length = sb.length();

        if (length > 0) {
            sb.setLength(length - 1);
        }

        return sb.toString();

    }

}