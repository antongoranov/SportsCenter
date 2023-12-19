package com.sportscenter.service;

import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.mapper.UserRoleMapper;
import com.sportscenter.model.view.UserRoleViewModel;
import com.sportscenter.repository.UserRoleRepository;
import com.sportscenter.service.impl.UserRoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceImplTest {

    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserRoleMapper userRoleMapper;

    @InjectMocks
    private UserRoleServiceImpl userRoleServiceMock;

    @Test
    void testFindAll() {

        List<UserRoleEntity> mockUserRoles = List.of(new UserRoleEntity());
        when(userRoleRepository.findAll())
                .thenReturn(mockUserRoles);

        Set<UserRoleViewModel> mockUserRoleViewModels = Set.of(new UserRoleViewModel());
        when(userRoleMapper.userRoleEntityToViewModel(any(UserRoleEntity.class)))
                .thenReturn(mockUserRoleViewModels.iterator().next());


        Set<UserRoleViewModel> result = userRoleServiceMock.findAll();


        assertEquals(mockUserRoles.size(), result.size());
        verify(userRoleMapper, times(mockUserRoles.size())).userRoleEntityToViewModel(any());
    }

}
