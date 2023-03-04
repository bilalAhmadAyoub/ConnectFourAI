import java.util.HashMap;

class IntArrayHashMap {

    private HashMap<IntArrayWrapper, Integer> map = new HashMap<>();

    public void put(int[][] key, int value) {
        map.put(new IntArrayWrapper(key), value);
    } //put

    public Integer get(int[][] key) {
        return map.get(new IntArrayWrapper(key));
    } //get

    public int size() {
        return map.size();
    } //size

    public boolean containsKey(int[][] key) {
        return map.containsKey(new IntArrayWrapper(key));
    } //containsKey

    public static class IntArrayWrapper {
        private final int[][] arr;

        public IntArrayWrapper(int[][] arr) {
            this.arr = arr;
        } //IntArrayWrapper

        @Override
        public int hashCode() {
            int hashCode = java.util.Arrays.deepHashCode(arr);
            return hashCode;
        } //hashCode

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntArrayWrapper that = (IntArrayWrapper) o;
            return java.util.Arrays.deepEquals(arr, that.arr);
        } //equals
    } //IntArrayWrapper
} //IntArrayHashMap


