package de.mensa_uds.android.data;

import java.util.Arrays;

public class SpecialString {

    private String value;
    private String[] additional;

    public SpecialString() {
        additional = new String[]{};
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getAdditional() {
        return additional;
    }

    public void setAdditional(String[] additional) {
        this.additional = additional;
    }

    @Override
    public String toString() {
        return "SpecialString [value=" + value + ", additional=" + Arrays.toString(additional) + "]";
    }

}
