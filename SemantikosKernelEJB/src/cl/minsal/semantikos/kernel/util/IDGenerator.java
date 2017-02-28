package cl.minsal.semantikos.kernel.util;

/**
 * Created by des01c7 on 28-02-17.
 */
public class IDGenerator {

    private static final String ID_SNOMED_CT_CHILE = "1000167";
    public static final int TYPE_CONCEPT = 10;
    public static final int TYPE_DESCRIPTION = 11;
    public static final int TYPE_RELATIONSHIP = 12;

    public static String generator(String id, int type) {
        String result = id + ID_SNOMED_CT_CHILE;
        if (type == TYPE_CONCEPT) {
            result = result + TYPE_CONCEPT;
        } else {
            if (type == TYPE_DESCRIPTION) {
                result = result + TYPE_DESCRIPTION;
            } else {
                if (type == TYPE_RELATIONSHIP) {
                    result = result + TYPE_RELATIONSHIP;
                }
            }
        }
        result = result + generateVerhoeff(result);
        return result;
    }

    private static int[][] d = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
                    {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
                    {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
                    {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
                    {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
                    {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
                    {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
                    {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
                    {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
            };

    private static int[][] p = new int[][]
            {
                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
                    {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
                    {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
                    {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
                    {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
                    {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
                    {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
            };


    private static int[] inv = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};


    private static String generateVerhoeff(String num) {

        int c = 0;
        int[] myArray = stringToReversedIntArray(num);

        for (int i = 0; i < myArray.length; i++) {
            c = d[c][p[((i + 1) % 8)][myArray[i]]];
        }

        return Integer.toString(inv[c]);
    }


    private static boolean validateVerhoeff(String num) {

        int c = 0;
        int[] myArray = stringToReversedIntArray(num);

        for (int i = 0; i < myArray.length; i++) {
            c = d[c][p[(i % 8)][myArray[i]]];
        }

        return (c == 0);
    }

    private static int[] stringToReversedIntArray(String num) {

        int[] myArray = new int[num.length()];

        for (int i = 0; i < num.length(); i++) {
            myArray[i] = Integer.parseInt(num.substring(i, i + 1));
        }

        myArray = reverse(myArray);

        return myArray;

    }


    private static int[] reverse(int[] myArray) {
        int[] reversed = new int[myArray.length];

        for (int i = 0; i < myArray.length; i++) {
            reversed[i] = myArray[myArray.length - (i + 1)];
        }

        return reversed;
    }
}
