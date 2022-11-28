package ElGamal;

import java.util.Random;

// Alice wants to send a message to Bob

public class Elgamal {
    static volatile int m;
    static int p =6661;
    static int g =666;
    static int BobPk = 2227;
    static int c;
    
    // inicialize an array to transport the cipher and Alice's
    // public key, so that Bob can be able to reconstruct the message.
    static volatile int[] response = new int[2];
    public static void main(String [] args){

        Elgamal ob = new Elgamal();
        Random rand = new Random();

       // Alice calls encrypte to send a message to Bob and chooses a random sk.
       int aliceSk = rand.nextInt(6660);
        ob.AliceEncrypt(aliceSk);

        // Eve finds Bob Sk and reads the message
        ob.EveAdversary();

       // unable to read the message Mallory malicious change alice's message before it gets to Bob.
        ob.MalloryAdversary();

        // Bob call decrypte and reads modified message.
        ob.bobDecrypt();
    }

    // Compute method to calculate g^mod p it
    private static int compute(int a, int base){
        return base^a % p;
    }

    // cipher method to encrypt a message with a given shared key
    private static int cypher(int m, int key){
        return key*m;
    }

    private static void AliceEncrypt(int aliceSk){
      //Alice choose a random sk and computes her public key.
      int AlicePk = compute(aliceSk, g);
      // Alice writes the message to the m variable
      m = 2000;
      //Alice computes the shared Key
      int sharedKey = compute(aliceSk, BobPk);
      //Alice encrypte the message with the sharedKey
      c = cypher(m, sharedKey);
      //Finally Alice places her PK and the cipherText in array to send to Bob
      
          response[0]=AlicePk;
          response[1]= c;
      
     
     
      System.out.println("Alice is sending a confidencial message to Bob... \n");

  }

    private static void bobDecrypt(){
        int key, bobSk=0;
        // Bob wants to Decrypt the message
        // First: Get the Alice's Public key
        int AlicePk = response[0];
        // Second: Get the cipherText which is the message encrypted with the shared key.
        int CypherText = response[1];
        
        //I find Bob sk buy using his PK, normally he should know his Sk because he would choose randomly
        //however for the current exercise I brute forced.
        for (int i = 0; i< p; i++){
            key = compute(i, g);
            
            if(BobPk == key){
                bobSk = i;
            
            }
        }
        //Bob computes the shared key with his sk and Alice's Pk(g^y) to get (g^y)^x.
        int sharedKey = compute(bobSk, AlicePk);

        // Finally he desconstruct the message computing 
        int Message = CypherText/sharedKey;
        System.out.println("Bob decrypts Alice's message and reads: "+ Message);
   
    }

    

   // Exercise number 2 Eve intersepted alice's message to Bob and 
   //finds Bob sk to try to open the message
    private static void EveAdversary(){

        // For definition adversary can see the comunication between
        // Bob and Alice but can'read the message withoud the key.
        // I will let Eve read the response array and get the cypher and Alice's Pk.
        int aliceEncryMsg; 
        int alicePk;
        int bobSk = 0;
        int key;
        //Eve reads the data Alice is sending to Bob
        
            aliceEncryMsg = response[1];
            alicePk = response[0];
      
        // Eve brute force to find Bob sk
        for (int i = 0; i< p; i++){
            key = compute(i, g);
            
            if(BobPk == key){
                bobSk = i;
            
            }
        }
        // Eve computes sharedKey 
        int sharedKey = compute(alicePk, bobSk);
        //Finally computes the message
        int Message = aliceEncryMsg/sharedKey;
        System.out.println("Before the message gets to Bob Eve(the adversary) \n reads Alice's Message by Brute forcing BobSK and she reads : " + Message+"\n");

    }

    // Exercise nr3 MAllory changes Alice's message before Bob reads
    private static void MalloryAdversary(){
      //Mallory gets the Encripted data unable to read the message.
        int aliceEncryMsg = response[1]; 
        System.out.println("After that Mallory malicious changes Alice's message before it gets to Bob \n");
        //Finally changes the message without reading.
        
            response[1] = aliceEncryMsg * 3;    


    }

     
}
