public class HugeInteger {
    private static final int MAX_DIGITS = 40;
    private int[] digits = new int[MAX_DIGITS];
    private boolean isNegative = false;

    public void parse(String number) {
        isNegative = false;
    
        if (number.length() == 0) {
            throw new IllegalArgumentException("Empty input.");
        }
    
        if (number.charAt(0) == '-') {
            isNegative = true;
            number = number.substring(1); // 移除負號
        }
    
        int len = number.length(); // 重新取長度（去掉負號後）
        if (len > MAX_DIGITS) {
            throw new IllegalArgumentException("Number too large (max 40 digits).");
        }
    
        // 從字串右邊開始存入 digits 陣列
        for (int i = 0; i < len; i++) {
            char ch = number.charAt(len - 1 - i);
            if (Character.isDigit(ch)) {
                digits[MAX_DIGITS - 1 - i] = ch - '0';
            } else {
                throw new IllegalArgumentException("Invalid character: " + ch);
            }
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isNegative && !isZero()) sb.append('-');

        boolean leadingZero = true;
        for (int digit : digits) {
            if (digit != 0 || !leadingZero) {
                sb.append(digit);
                leadingZero = false;
            }
        }
        return leadingZero ? "0" : sb.toString();
    }

    public HugeInteger add(HugeInteger other) {
        if (this.isNegative == other.isNegative) {
            HugeInteger result = addAbs(this, other);
            result.isNegative = this.isNegative;
            return result;
        } else {
            if (compareAbs(this, other) >= 0) {
                HugeInteger result = subtractAbs(this, other);
                result.isNegative = this.isNegative;
                return result;
            } else {
                HugeInteger result = subtractAbs(other, this);
                result.isNegative = other.isNegative;
                return result;
            }
        }
    }

    public HugeInteger subtract(HugeInteger other) {
        HugeInteger negOther = new HugeInteger();
        negOther.digits = other.digits.clone();
        negOther.isNegative = !other.isNegative;
        return this.add(negOther);
    }

    public boolean isEqualTo(HugeInteger other) {
        if (this.isNegative != other.isNegative) return false;
        for (int i = 0; i < MAX_DIGITS; i++) {
            if (this.digits[i] != other.digits[i]) return false;
        }
        return true;
    }

    public boolean isNotEqualTo(HugeInteger other) {
        return !isEqualTo(other);
    }

    public boolean isGreaterThan(HugeInteger other) {
        if (this.isNegative && !other.isNegative) return false;
        if (!this.isNegative && other.isNegative) return true;

        int cmp = compareAbs(this, other);
        return this.isNegative ? (cmp < 0) : (cmp > 0);
    }

    public boolean isLessThan(HugeInteger other) {
        return !isGreaterThan(other) && !isEqualTo(other);
    }

    public boolean isGreaterThanOrEqualTo(HugeInteger other) {
        return isGreaterThan(other) || isEqualTo(other);
    }

    public boolean isLessThanOrEqualTo(HugeInteger other) {
        return isLessThan(other) || isEqualTo(other);
    }

    public boolean isZero() {
        for (int digit : digits) {
            if (digit != 0) return false;
        }
        return true;
    }

    private HugeInteger addAbs(HugeInteger a, HugeInteger b) {
        HugeInteger result = new HugeInteger();
        int carry = 0;
        for (int i = MAX_DIGITS - 1; i >= 0; i--) {
            int sum = a.digits[i] + b.digits[i] + carry;
            result.digits[i] = sum % 10;
            carry = sum / 10;
        }
        return result;
    }

    private HugeInteger subtractAbs(HugeInteger a, HugeInteger b) {
        HugeInteger result = new HugeInteger();
        int borrow = 0;
        for (int i = MAX_DIGITS - 1; i >= 0; i--) {
            int diff = a.digits[i] - b.digits[i] - borrow;
            if (diff < 0) {
                diff += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }
            result.digits[i] = diff;
        }
        return result;
    }

    private int compareAbs(HugeInteger a, HugeInteger b) {
        for (int i = 0; i < MAX_DIGITS; i++) {
            if (a.digits[i] > b.digits[i]) return 1;
            if (a.digits[i] < b.digits[i]) return -1;
        }
        return 0;
    }
}
