package be.api.services;

import be.api.dto.request.UserRequestDTO;
import be.api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserServices {
    int saveUser(UserRequestDTO userDTO);
    void updateUser(int id, UserRequestDTO userDTO);
    UserRequestDTO getUserById(int id);
    List<UserRequestDTO> getListUserByPaging(int pageNo, int pageSize);
    void deleteUser(int id);
    List<UserRequestDTO> searchUsersByName(String name, int pageNo, int pageSize);
    User getUserByEmail(String email);
    User getInfoUser();
}
