package com.example.openbooksspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UsersRepository {
    private List<User> users = new ArrayList<>();

    boolean addUser(User user){
        return users.add(user);
    }
    boolean removeUser(User user){
        return users.remove(user);
    }

    boolean canAddUser(User userData){
        for(User user : users)
            if (user.getLogin().equals(userData.getLogin())) return false;
        return true;
    }

    boolean hasAuthor(String autorLogin){
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
