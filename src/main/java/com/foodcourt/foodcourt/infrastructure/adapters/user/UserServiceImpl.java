package com.foodcourt.foodcourt.infrastructure.adapters.user;

import com.foodcourt.foodcourt.domain.gateways.UserServiceGateway;
import com.foodcourt.foodcourt.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceGateway {
	
	@Override
	public User findById(Long id) {
		return null;
	}
}
