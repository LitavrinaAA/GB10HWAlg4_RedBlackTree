public class Main {
    public static void main(String[] args) {

        RBTree<Integer> rBTree = new RBTree<Integer>();
//        rBTree.insert(10).insert(20).insert(5).insert(40).insert(50).insert(25).insert(60).insert(80).insert(85)
//                .insert(90).insert(30).insert(15).insert(75).insert(100).insert(55).insert(45).insert(0).insert(26);
        rBTree.insert(10).insert(20).insert(5).insert(40).insert(45).insert(0).insert(-1);
        rBTree.print();


    }

}