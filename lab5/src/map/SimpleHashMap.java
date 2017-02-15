package map;


public class SimpleHashMap<K, V> implements Map<K, V>{

    Entry<K,V>[] table;
    int size;
    float loadFactor;

    public SimpleHashMap() {
        table = (Entry<K, V>[]) new Entry[16];
        size = 0;
        loadFactor = 0.75f;
    }

    public SimpleHashMap(int capacity) {
        table = (Entry<K, V>[]) new Entry[capacity];
        size = 0;
        loadFactor = 0.75f;
    }

    @Override
    public V get(Object arg0) {
        K key = (K) arg0;

        Entry<K, V> found = find(index(key), key);
        if (found != null) return found.value;

        return null;
    }

    @Override
    public boolean isEmpty() {
        if(size == 0) return true;
        else return false;
    }

    @Override
    public V put(K arg0, V arg1) {
        int index = index(arg0);

        Entry<K, V> node = find(index, arg0);
        if (node != null) {
            V oldValue = node.value;
            node.value = arg1;
            return oldValue;
        }
        else {
            Entry<K, V> newNode = new Entry<>(arg0, arg1);
            newNode.next = table[index];
            table[index] = newNode;
            size++;
            if (((float)size) / table.length > loadFactor) {
                rehash();
            }
            return null;
        }
    }

    @Override
    public V remove(Object arg0) {
        K key = (K) arg0;
        int index = index(key);

        Entry<K, V> node = table[index];
        if (node != null) {
            if (node.key.equals(key)) {
                table[index] = node.next;
                size--;
                return node.value;
            } else {
                while (node.next != null) {
                    if (node.next.key.equals(key)) {
                        V value = node.next.value;
                        node.next = node.next.next;
                        size--;
                        return value;
                    }
                    node = node.next;
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    public String show(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < table.length; i++){
            Entry<K, V> e = table[i];
            if(e != null) {
                sb.append(i + "\t");
                do {
                    sb.append(e.toString() + " ");
                    e = e.next;
                } while (e != null);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private int index(K key){
        int index= key.hashCode() % table.length;
        if(index < 0) index = -index;
        return index;
    }

    private Entry<K, V> find(int index, K key){
        Entry<K, V> node = table[index];
        while(node != null){
            if(node.key.equals(key)){
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void rehash(){ // ska den få en egen put metod???
        Entry<K, V>[] oldTable = table;
        table = (Entry<K, V>[])new Entry[table.length * 2];
        size = 0;
        for(Entry<K, V> e: oldTable){
            while(e != null){
                put(e.key, e.value);
                e = e.next;
            }
        }
    }

    private static class Entry<K, V> implements Map.Entry<K, V>{

        private K key;
        private V value;
        Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Entry<K, V> getNext() {
            return next;
        }

        public void setNext(Entry<K, V> next) {
            this.next = next;
        }

        public  boolean hasNext(){return next != null;}

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value; //ska den retunera nya eller gamla värdet
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }
    }
}
