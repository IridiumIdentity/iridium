/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
package software.iridium.api.mapper;

import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class IdentityEntityMapper {

	public IdentityResponse map(final IdentityEntity entity) {
		if (entity == null) {
			return null;
		}
		
		final var response = new IdentityResponse();
		response.setId(entity.getId());
		response.setUsername(entity.getPrimaryEmail().getEmailAddress());
		response.getRoles().addAll(
				entity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList())
		);
		return response;
	}
}

