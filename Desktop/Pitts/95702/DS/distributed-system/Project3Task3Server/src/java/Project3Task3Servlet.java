/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * servlet class
 * @author apple
 */
@WebServlet(name = "Project3Task3Servlet",urlPatterns = {"/Project3Task3Servlet/*"})
public class Project3Task3Servlet extends HttpServlet {
 private static BlockChain bc;
    /**
     * public constructor for initializing the block chain by adding first block
     * @throws NoSuchAlgorithmException exception thrown
     */
    public Project3Task3Servlet() throws NoSuchAlgorithmException {
        bc = new BlockChain();
        Block genesis = new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2, "");
        bc.addBlock(genesis);
    }
    

    /**
     * do get method
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Console: doGet visited");
        String result = "";
        
        // The name is on the path /name so skip over the '/'
        String name = (request.getPathInfo()).substring(1);
        PrintWriter out = response.getWriter();
        //verify method         
        if (name.equals("verify")) {
            try {
                if (bc.isChainValid()) {
                    result = "The verify result is true";
                } else {
                    result = "The verify result is false";
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Project3Task3Servlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println(result);
        }
        //view method
        if (name.equals("view")) {
            out.println(bc.toString());
        }

        // Things went well so set the HTTP response code to 200 OK
        response.setStatus(200);
        // tell the client the type of the response
        response.setContentType("text/plain;charset=UTF-8");

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // POST is used to handle add block operation
    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        
        try {
            System.out.println("Console: doPost visited");
            
            // To look at what the client accepts examine request.getHeader("Accept")
            // We are not using the accept header here.
            
            // Read what the client has placed in the POST data area
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            
            
            
            // extract value from client side
            String[] data = br.readLine().split(",");
            String transaction = data[0];
            String diff = data[1];
            int level = Integer.parseInt(diff);
            if(transaction.equals("") || diff.equals("")) {
                // missing input return 401
                response.setStatus(401);    
                return;
            }
            
            String result = "";
            long t1 = System.currentTimeMillis();
            String[] tmpData = transaction.split("#");
            BigInteger e = new BigInteger("65537");
            BigInteger n = new BigInteger("2688520255179015026237478731436571621031218154515572968727588377065598663770912513333018006654248650656250913110874836607777966867106290192618336660849980956399732967369976281500270286450313199586861977623503348237855579434471251977653662553");
            BigInteger encrypted = new BigInteger(tmpData[1]);
            BigInteger decrytped = encrypted.modPow(e,n);
            String info = tmpData[0];
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(info.getBytes(StandardCharsets.UTF_8));
            byte[] copyHash = new byte[hash.length+1];
            System.arraycopy(hash,0,copyHash,1,hash.length);
            copyHash[0] = 0;
            if(decrytped.equals(new BigInteger(copyHash))){
                Block block = new Block(bc.getChainSize(), new Timestamp(System.currentTimeMillis()),transaction, level, bc.getLatestBlock().proofOfWork());
                bc.addBlock(block);
                long t2 = System.currentTimeMillis();
            result = "Total execution time required to add this block was " + (t2 - t1) + " milliseconds";
                
                // prepare response code
                response.setStatus(200);
                out.println(result);
                
                
            }else{
                result = "Your transaction is not accepted";
            
                out.println(result);
            }
            /* In this example, we use Put to update an existing variable.  */
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Project3Task3Servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
