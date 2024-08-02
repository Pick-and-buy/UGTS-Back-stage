package com.ugts.user.mapper;

import com.ugts.authentication.dto.request.RegisterRequest;
import com.ugts.brand.dto.GeneralBrandInformationDto;
import com.ugts.brand.entity.Brand;
import com.ugts.brandLine.dto.GeneralBrandLineInformationDto;
import com.ugts.brandLine.entity.BrandLine;
import com.ugts.comment.dto.GeneralCommentInformationDto;
import com.ugts.comment.entity.Comment;
import com.ugts.post.dto.LikedPostDto;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.post.entity.Post;
import com.ugts.product.dto.response.ProductImageResponse;
import com.ugts.product.dto.response.ProductResponse;
import com.ugts.product.entity.Product;
import com.ugts.product.entity.ProductImage;
import com.ugts.user.dto.GeneralUserInformationDto;
import com.ugts.user.dto.response.AddressResponse;
import com.ugts.user.dto.response.PermissionResponse;
import com.ugts.user.dto.response.RoleResponse;
import com.ugts.user.dto.response.UserResponse;
import com.ugts.user.entity.Address;
import com.ugts.user.entity.Permission;
import com.ugts.user.entity.Role;
import com.ugts.user.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    public UserResponse userToUserResponse(User user);
    public User register(RegisterRequest request);
}
