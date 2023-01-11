package com.volcengine.tos.internal.util;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.internal.model.CRC64Checksum;

import java.math.BigInteger;

public class CRC64Utils {
    private static final int GF2_DIM = 64;
    // 2^64 - 1
    private static final long MAX_VALUE = -1L;

    private static long gf2MatrixTimes(long[] mat, long vec) {
        long sum = 0;
        for(int i = 0; vec != 0; i++) {
            if ((vec & 1) == 1) {
                sum ^= mat[i];
            }
            vec >>>= 1;
        }
        return sum;
    }

    private static void gf2MatrixSquare(long[] square, long[] mat) {
        for (int i = 0; i < GF2_DIM; i++) {
            square[i] = gf2MatrixTimes(mat, mat[i]);
        }
    }

    public static CRC64Checksum combine(CRC64Checksum sum1, CRC64Checksum sum2, long len2) {
        if (len2 == 0) {
            return new CRC64Checksum(sum1.getValue());
        }
        return new CRC64Checksum(combine(sum1.getValue(), sum2.getValue(), len2));
    }

    public static long combine(long crc1, long crc2, long len2) {
        if (len2 == 0) {
            return crc1;
        }
        long row = 1;
        long[] even = new long[GF2_DIM];
        long[] odd = new long[GF2_DIM];

        odd[0] = CRC64Checksum.ECMA_POLY;

        for (int i = 1; i < GF2_DIM; i++) {
            odd[i] = row;
            row <<= 1;
        }

        gf2MatrixSquare(even, odd);
        gf2MatrixSquare(odd, even);

        do {
            gf2MatrixSquare(even, odd);
            if ((len2 & 1) == 1) {
                crc1 = gf2MatrixTimes(even, crc1);
            }
            len2 >>>= 1;

            if (len2 == 0) {
                break;
            }

            gf2MatrixSquare(odd, even);
            if ((len2 & 1) == 1) {
                crc1 = gf2MatrixTimes(odd, crc1);
            }
            len2 >>>= 1;
        } while (len2 != 0);

        crc1 ^= crc2;
        return crc1;
    }

    public static String longToUnsignedLongString(long x) {
        return longToUnsignedLongString(x, 10);
    }

    public static long unsignedLongStringToLong(String unsignedLong) {
        return parseUnsignedLong(unsignedLong, 10);
    }

    static String longToUnsignedLongString(long x, int radix) {
        if (x == 0L) {
            return "0";
        } else if (x > 0L) {
            return Long.toString(x, radix);
        } else {
            char[] buf = new char[64];
            int i = buf.length;
            if ((radix & radix - 1) == 0) {
                int shift = Integer.numberOfTrailingZeros(radix);
                int mask = radix - 1;

                do {
                    --i;
                    buf[i] = Character.forDigit((int)x & mask, radix);
                    x >>>= shift;
                } while(x != 0L);
            } else {
                long quotient;
                if ((radix & 1) == 0) {
                    quotient = (x >>> 1) / (long)(radix >>> 1);
                } else {
                    quotient = divide(x, (long)radix);
                }

                long rem = x - quotient * (long)radix;
                --i;
                buf[i] = Character.forDigit((int)rem, radix);

                for(x = quotient; x > 0L; x /= (long)radix) {
                    --i;
                    buf[i] = Character.forDigit((int)(x % (long)radix), radix);
                }
            }

            return new String(buf, i, buf.length - i);
        }
    }

    static long parseUnsignedLong(String unsignedLongString, int radix) {
        if (StringUtils.isEmpty(unsignedLongString)) {
            return 0;
        }

        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
            throw new TosClientException("illegal radix: " + radix, null);
        }

        int maxSafePos = ParseOverflowDetection.maxSafeDigits[radix] - 1;
        long value = 0;
        for (int pos = 0; pos < unsignedLongString.length(); pos++) {
            int digit = Character.digit(unsignedLongString.charAt(pos), radix);
            if (digit == -1) {
                throw new TosClientException("not found digit in unsigned long: " + unsignedLongString, null);
            }
            if (pos > maxSafePos && ParseOverflowDetection.overflowInParse(value, digit, radix)) {
                throw new TosClientException("too large for unsigned long: " + unsignedLongString, null);
            }
            value = (value * radix) + digit;
        }

        return value;
    }

    private static long divide(long dividend, long divisor) {
        if (divisor < 0L) {
            return compare(dividend, divisor) < 0 ? 0L : 1L;
        } else if (dividend >= 0L) {
            return dividend / divisor;
        } else {
            long quotient = (dividend >>> 1) / divisor << 1;
            long rem = dividend - quotient * divisor;
            return quotient + (long)(compare(rem, divisor) >= 0 ? 1 : 0);
        }
    }

    private static int compare(long a, long b) {
        return Long.compare(flip(a), flip(b));
    }

    private static long flip(long a) {
        return a ^ -9223372036854775808L;
    }

    private static final class ParseOverflowDetection {
        private ParseOverflowDetection() {}

        // calculated as 0xffffffffffffffff / radix
        static final long[] maxValueDivs = new long[Character.MAX_RADIX + 1];
        static final int[] maxValueMods = new int[Character.MAX_RADIX + 1];
        static final int[] maxSafeDigits = new int[Character.MAX_RADIX + 1];

        static {
            BigInteger overflow = new BigInteger("10000000000000000", 16);
            for (int i = Character.MIN_RADIX; i <= Character.MAX_RADIX; i++) {
                maxValueDivs[i] = divide(MAX_VALUE, i);
                maxValueMods[i] = (int) remainder(MAX_VALUE, i);
                maxSafeDigits[i] = overflow.toString(i).length() - 1;
            }
        }

        static boolean overflowInParse(long current, int digit, int radix) {
            if (current >= 0) {
                if (current < maxValueDivs[radix]) {
                    return false;
                }
                if (current > maxValueDivs[radix]) {
                    return true;
                }
                return (digit > maxValueMods[radix]);
            }

            return true;
        }

        static long remainder(long dividend, long divisor) {
            if (divisor < 0) {
                if (compare(dividend, divisor) < 0) {
                    return dividend;
                }
                return dividend - divisor;
            }

            if (dividend >= 0) {
                return dividend % divisor;
            }

            long quotient = ((dividend >>> 1) / divisor) << 1;
            long rem = dividend - quotient * divisor;
            return rem - (compare(rem, divisor) >= 0 ? divisor : 0);
        }
    }
}
