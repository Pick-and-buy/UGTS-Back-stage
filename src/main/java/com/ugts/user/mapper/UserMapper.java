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

//@Mapper(componentModel = "spring")
@Component
public class UserMapper {
    public User register(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        if ( request.getId() != null ) {
            user.id( String.valueOf( request.getId() ) );
        }
        user.username( request.getUsername() );
        user.password( request.getPassword() );
        user.lastName( request.getLastName() );
        user.firstName( request.getFirstName() );
        user.email( request.getEmail() );
        user.phoneNumber( request.getPhoneNumber() );
        user.dob( request.getDob() );

        return user.build();
    }

    public UserResponse userToUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.username( user.getUsername() );
        userResponse.avatar( user.getAvatar() );
        userResponse.lastName( user.getLastName() );
        userResponse.firstName( user.getFirstName() );
        userResponse.email( user.getEmail() );
        userResponse.phoneNumber( user.getPhoneNumber() );
        userResponse.dob( user.getDob() );
        userResponse.address( addressSetToAddressResponseSet( user.getAddress() ) );
        userResponse.roles( roleSetToRoleResponseSet( user.getRoles() ) );
        userResponse.likedPosts( postSetToLikedPostDtoSet( user.getLikedPosts() ) );
        userResponse.createdPosts( postSetToPostResponseSet( user.getCreatedPosts() ) );
        userResponse.isVerified( user.isVerified() );

        return userResponse.build();
    }
    protected AddressResponse addressToAddressResponse(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponse addressResponse = new AddressResponse();

        addressResponse.setId( address.getId() );
        addressResponse.setStreet( address.getStreet() );
        addressResponse.setDistrict( address.getDistrict() );
        addressResponse.setProvince( address.getProvince() );
        addressResponse.setCountry( address.getCountry() );
        addressResponse.setAddressLine( address.getAddressLine() );
        addressResponse.setDefault( address.isDefault() );

        return addressResponse;
    }

    protected Set<AddressResponse> addressSetToAddressResponseSet(Set<Address> set) {
        if ( set == null ) {
            return null;
        }

        Set<AddressResponse> set1 = new LinkedHashSet<AddressResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Address address : set ) {
            set1.add( addressToAddressResponse( address ) );
        }

        return set1;
    }

    protected PermissionResponse permissionToPermissionResponse(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionResponse.PermissionResponseBuilder permissionResponse = PermissionResponse.builder();

        permissionResponse.name( permission.getName() );
        permissionResponse.description( permission.getDescription() );

        return permissionResponse.build();
    }

    protected Set<PermissionResponse> permissionSetToPermissionResponseSet(Set<Permission> set) {
        if ( set == null ) {
            return null;
        }

        Set<PermissionResponse> set1 = new LinkedHashSet<PermissionResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Permission permission : set ) {
            set1.add( permissionToPermissionResponse( permission ) );
        }

        return set1;
    }

    protected RoleResponse roleToRoleResponse(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleResponse.RoleResponseBuilder roleResponse = RoleResponse.builder();

        roleResponse.name( role.getName() );
        roleResponse.description( role.getDescription() );
        roleResponse.permissions( permissionSetToPermissionResponseSet( role.getPermissions() ) );

        return roleResponse.build();
    }

    protected Set<RoleResponse> roleSetToRoleResponseSet(Set<Role> set) {
        if ( set == null ) {
            return null;
        }

        Set<RoleResponse> set1 = new LinkedHashSet<RoleResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Role role : set ) {
            set1.add( roleToRoleResponse( role ) );
        }

        return set1;
    }

    protected LikedPostDto postToLikedPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        LikedPostDto likedPostDto = new LikedPostDto();

        likedPostDto.setId( post.getId() );
        likedPostDto.setTitle( post.getTitle() );
        likedPostDto.setIsAvailable( post.getIsAvailable() );

        return likedPostDto;
    }

    protected Set<LikedPostDto> postSetToLikedPostDtoSet(Set<Post> set) {
        if ( set == null ) {
            return null;
        }

        Set<LikedPostDto> set1 = new LinkedHashSet<LikedPostDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Post post : set ) {
            set1.add( postToLikedPostDto( post ) );
        }

        return set1;
    }

    protected GeneralUserInformationDto userToGeneralUserInformationDto(User user) {
        if ( user == null ) {
            return null;
        }

        GeneralUserInformationDto generalUserInformationDto = new GeneralUserInformationDto();

        generalUserInformationDto.setId( user.getId() );
        generalUserInformationDto.setUsername( user.getUsername() );
        generalUserInformationDto.setAvatar( user.getAvatar() );
        generalUserInformationDto.setPhoneNumber( user.getPhoneNumber() );
        generalUserInformationDto.setFirstName( user.getFirstName() );
        generalUserInformationDto.setLastName( user.getLastName() );
        generalUserInformationDto.setVerified( user.isVerified() );

        return generalUserInformationDto;
    }

    protected ProductImageResponse productImageToProductImageResponse(ProductImage productImage) {
        if ( productImage == null ) {
            return null;
        }

        ProductImageResponse productImageResponse = new ProductImageResponse();

        productImageResponse.setId( productImage.getId() );
        productImageResponse.setImageUrl( productImage.getImageUrl() );

        return productImageResponse;
    }

    protected Set<ProductImageResponse> productImageListToProductImageResponseSet(List<ProductImage> list) {
        if ( list == null ) {
            return null;
        }

        Set<ProductImageResponse> set = new LinkedHashSet<ProductImageResponse>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( ProductImage productImage : list ) {
            set.add( productImageToProductImageResponse( productImage ) );
        }

        return set;
    }

    protected GeneralBrandInformationDto brandToGeneralBrandInformationDto(Brand brand) {
        if ( brand == null ) {
            return null;
        }

        GeneralBrandInformationDto generalBrandInformationDto = new GeneralBrandInformationDto();

        generalBrandInformationDto.setId( brand.getId() );
        generalBrandInformationDto.setName( brand.getName() );

        return generalBrandInformationDto;
    }

    protected GeneralBrandLineInformationDto brandLineToGeneralBrandLineInformationDto(BrandLine brandLine) {
        if ( brandLine == null ) {
            return null;
        }

        GeneralBrandLineInformationDto generalBrandLineInformationDto = new GeneralBrandLineInformationDto();

        generalBrandLineInformationDto.setId( brandLine.getId() );
        generalBrandLineInformationDto.setLineName( brandLine.getLineName() );
        generalBrandLineInformationDto.setDescription( brandLine.getDescription() );
        generalBrandLineInformationDto.setLaunchDate( brandLine.getLaunchDate() );
        generalBrandLineInformationDto.setSignatureFeatures( brandLine.getSignatureFeatures() );
        generalBrandLineInformationDto.setPriceRange( brandLine.getPriceRange() );
        generalBrandLineInformationDto.setAvailableStatus( brandLine.getAvailableStatus() );

        return generalBrandLineInformationDto;
    }

    protected ProductResponse productToProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse productResponse = new ProductResponse();

        productResponse.setId( product.getId() );
        productResponse.setName( product.getName() );
        productResponse.setImages( productImageListToProductImageResponseSet( product.getImages() ) );
        productResponse.setBrand( brandToGeneralBrandInformationDto( product.getBrand() ) );
        productResponse.setBrandLine( brandLineToGeneralBrandLineInformationDto( product.getBrandLine() ) );
        productResponse.setBrandCollection( product.getBrandCollection() );
        productResponse.setCategory( product.getCategory() );
        productResponse.setPrice( product.getPrice() );
        productResponse.setColor( product.getColor() );
        productResponse.setSize( product.getSize() );
        productResponse.setWidth( product.getWidth() );
        productResponse.setHeight( product.getHeight() );
        productResponse.setLength( product.getLength() );
        productResponse.setReferenceCode( product.getReferenceCode() );
        productResponse.setManufactureYear( product.getManufactureYear() );
        productResponse.setExteriorMaterial( product.getExteriorMaterial() );
        productResponse.setInteriorMaterial( product.getInteriorMaterial() );
        if ( product.getCondition() != null ) {
            productResponse.setCondition( product.getCondition().name() );
        }
        productResponse.setAccessories( product.getAccessories() );
        productResponse.setDateCode( product.getDateCode() );
        productResponse.setSerialNumber( product.getSerialNumber() );
        productResponse.setPurchasedPlace( product.getPurchasedPlace() );
        productResponse.setStory( product.getStory() );
        if ( product.getVerifiedLevel() != null ) {
            productResponse.setVerifiedLevel( product.getVerifiedLevel().name() );
        }

        return productResponse;
    }

    protected GeneralCommentInformationDto commentToGeneralCommentInformationDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        GeneralCommentInformationDto generalCommentInformationDto = new GeneralCommentInformationDto();

        generalCommentInformationDto.setUser( userToGeneralUserInformationDto( comment.getUser() ) );
        generalCommentInformationDto.setId( comment.getId() );
        generalCommentInformationDto.setCommentContent( comment.getCommentContent() );
        generalCommentInformationDto.setCreateAt( comment.getCreateAt() );

        return generalCommentInformationDto;
    }

    protected Set<GeneralCommentInformationDto> commentSetToGeneralCommentInformationDtoSet(Set<Comment> set) {
        if ( set == null ) {
            return null;
        }

        Set<GeneralCommentInformationDto> set1 = new LinkedHashSet<GeneralCommentInformationDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Comment comment : set ) {
            set1.add( commentToGeneralCommentInformationDto( comment ) );
        }

        return set1;
    }

    protected PostResponse postToPostResponse(Post post) {
        if ( post == null ) {
            return null;
        }

        PostResponse postResponse = new PostResponse();

        postResponse.setUser( userToGeneralUserInformationDto( post.getUser() ) );
        postResponse.setId( post.getId() );
        postResponse.setTitle( post.getTitle() );
        postResponse.setDescription( post.getDescription() );
        postResponse.setIsAvailable( post.getIsAvailable() );
        postResponse.setCreatedAt( post.getCreatedAt() );
        postResponse.setUpdatedAt( post.getUpdatedAt() );
        postResponse.setProduct( productToProductResponse( post.getProduct() ) );
        postResponse.setComments( commentSetToGeneralCommentInformationDtoSet( post.getComments() ) );

        return postResponse;
    }

    protected Set<PostResponse> postSetToPostResponseSet(Set<Post> set) {
        if ( set == null ) {
            return null;
        }

        Set<PostResponse> set1 = new LinkedHashSet<PostResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Post post : set ) {
            set1.add( postToPostResponse( post ) );
        }

        return set1;
    }

}
