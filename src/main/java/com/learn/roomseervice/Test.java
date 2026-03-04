package com.learn.roomseervice;

public class Test {
    record Room(int roomNumber, String roomMateName,Boolean binStatus,Boolean CommonAreaCleanStatus){
        Room(int roomNumber) {
            this(roomNumber,"",false,false);
        }


    };
    static Room room1=new Room(1,"Bhakti",false,false);
    static Room room2=new Room(2,"Shreyas",false,true);
    static Room room3=new Room(3,"Krupa",true,false);

    record ListNode(Room room,ListNode next){
        ListNode(Room room){
            this(room, new ListNode());
        }

        public ListNode() {
            this(null, new ListNode());
        }
    };
    ListNode insertAtHead(ListNode head, Room room) {
        ListNode newNode = new ListNode(room,head);
        return newNode;   // new head
    }

    public static void main(String s){
        Test t= new Test();
        ListNode head = null;

//        head = t.insertAtHead(head, room1);
//        head = t.insertAtHead(head, room2);
//        head = t.insertAtHead(head, room3);

        t.sendPushNotification();
    }
    private void sendPushNotification() {
        ListNode start = new ListNode(room1,new ListNode(room2,new ListNode(room3,new ListNode(null))));
        ListNode current = start;

        while(start != null){

        }

        //while()
    }
}
