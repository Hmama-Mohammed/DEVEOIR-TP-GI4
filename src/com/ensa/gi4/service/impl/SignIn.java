package com.ensa.gi4.service.impl;

        import com.ensa.gi4.datatabase.impl.GenericDAO;
        import com.ensa.gi4.datatabase.impl.UserRowMapper;
        import com.ensa.gi4.modele.User;
        import com.ensa.gi4.service.api.ISignIn;
        import org.springframework.jdbc.core.RowMapper;
        import org.springframework.stereotype.Component;

        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.util.Scanner;

@Component
//@Aspect
public class SignIn extends GenericDAO<User> implements ISignIn {

    String username;
    String password;

    public User signInPerson()
    {

        while(true)
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("username : ");
            username = scanner.next();
            System.out.println("password : ");
            password = scanner.next();
            password = hashMDP(password);

            try{
                User user = super.findRealPassword("SELECT * FROM USER WHERE username=?;", username);
                String realPassword = user.getPassword();
                    realPassword = hashMDP(realPassword);
                    if(realPassword.equals(password))
                    {
                        return user;
                    }
                    else
                    {
                        System.out.println("password incorrect");
                    }


            }
            catch(Exception e){
                System.out.println("user not found");
                System.out.println("1- pour essayer une nouvelle fois, entrer 1");
                System.out.println("0- pour sortir de l'application, entrer 0");
                String next = scanner.next();
                if ("0".equals(next)) {
                    System.exit(0);
                }
            }
        }

    }


    //Methode pour hashes le mot de passe
    public String hashMDP(String password)
    {
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(password.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for(byte b : resultByteArray)
            {
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    protected RowMapper<User> getRowMapper() {
        return new UserRowMapper();
    }
}
