package com.example.openbooksspring.repositories;

import com.example.openbooksspring.classes.User;
import com.example.openbooksspring.helpers.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UsersRepository {
    private List<User> users = new ArrayList<>();

    @Autowired TokenRepository tokenRepository;

    public boolean addUser(User user){
        return users.add(user);
    }
    boolean removeUser(User user){
        return users.remove(user);
    }

    public User login(String login, String password){
        for(User user : users)
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) return user;
        return null;
    }

    public boolean updateUser(String login, String password, String token){
        User user = getUserByToken(TokenHelper.getLoginByToken(token));

        return user.update(login, password);
    }

    public User getUserByToken(String token){
        return getUserByLogin(TokenHelper.getLoginByToken(token));
    }

    public boolean canAddUser(User userData){
        for(User user : users)
            if (user.getLogin().equals(userData.getLogin())) return false;
        return true;
    }

    public boolean hasAuthor(String autorLogin){
        for(User user : users)
            if (user.getLogin().equals(autorLogin)) return true;
        return false;
    }

    private User getUserByLogin(String login){
        for(User user : users) if (user.getLogin().equals(login)) return user;
        return null;
    }

    public List<User> getUsers() {
        return users;
    }
}
