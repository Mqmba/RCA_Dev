package be.api.services.impl;

import be.api.dto.request.UserRequestDTO;
import be.api.exception.ResourceNotFoundException;
import be.api.model.User;
import be.api.repository.IUserRepository;
import be.api.services.IUserServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServices implements IUserServices {

    public final IUserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public int saveUser(UserRequestDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepository.save(user);
        log.info("User added successfully");
        return user.getUserId();
    }

    @Override
    public void updateUser(int id, UserRequestDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        modelMapper.map(userDTO, user);
        userRepository.save(user);
        log.info("User updated successfully");
    }

    @Override
    public UserRequestDTO getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return modelMapper.map(user, UserRequestDTO.class);
    }

    @Override
    public List<UserRequestDTO> getListUserByPaging(int pageNo, int pageSize) {
        if(pageNo > 0){
            pageNo = pageNo - 1;
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> pageResult = userRepository.findAll(pageable);

        List<UserRequestDTO> userDTOs = pageResult.stream()
                .map(user -> modelMapper.map(user, UserRequestDTO.class))
                .collect(Collectors.toList());

        return userDTOs;
    }

    @Override
    public void deleteUser(int id) {
     try{
         User user = userRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

         userRepository.delete(user);
         log.info("User deleted successfully");
     }
        catch (Exception e){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    @Override
    public List<UserRequestDTO> searchUsersByName(String name, int pageNo, int pageSize) {
        try{
            if(pageNo > 0){
                pageNo = pageNo - 1;
            }
            Pageable pageable = PageRequest.of(pageNo, pageSize);

            Page<User> pageResult = userRepository.searchByName(name, pageable);
            return pageResult.stream()
                    .map(user -> modelMapper.map(user, UserRequestDTO.class))
                    .collect(Collectors.toList());
        }
        catch (Exception e){
            throw new ResourceNotFoundException("User not found with name: " + name);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User getInfoUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + userName);
        }
        return user;
    }




}
