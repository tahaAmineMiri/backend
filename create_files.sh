#!/bin/bash

BASE_DIR="src/main/java/com/incon/backend"

# Import boilerplate creation functions
source ./boilerplate_utils.sh

# === Create Files with Boilerplate Code ===

# config/
create_class "$BASE_DIR/config/SecurityConfig.java"
create_class "$BASE_DIR/config/JwtConfig.java"
create_class "$BASE_DIR/config/SwaggerConfig.java"

# controller/
create_class "$BASE_DIR/controller/AdminController.java"
create_class "$BASE_DIR/controller/AuthController.java"
create_class "$BASE_DIR/controller/CartController.java"
create_class "$BASE_DIR/controller/CompanyController.java"
create_class "$BASE_DIR/controller/DocumentController.java"
create_class "$BASE_DIR/controller/OrderController.java"
create_class "$BASE_DIR/controller/PaymentController.java"
create_class "$BASE_DIR/controller/ProductController.java"
create_class "$BASE_DIR/controller/ReviewController.java"
create_class "$BASE_DIR/controller/SubscriptionController.java"

# dto/request/
create_class "$BASE_DIR/dto/request/AdminVerificationRequest.java"
create_class "$BASE_DIR/dto/request/BuyerRegistrationRequest.java"
create_class "$BASE_DIR/dto/request/CartItemRequest.java"
create_class "$BASE_DIR/dto/request/CompanyRegistrationRequest.java"
create_class "$BASE_DIR/dto/request/LoginRequest.java"
create_class "$BASE_DIR/dto/request/OrderRequest.java"
create_class "$BASE_DIR/dto/request/PaymentRequest.java"
create_class "$BASE_DIR/dto/request/ProductRequest.java"
create_class "$BASE_DIR/dto/request/ReviewRequest.java"
create_class "$BASE_DIR/dto/request/SellerRegistrationRequest.java"
create_class "$BASE_DIR/dto/request/SubscriptionRequest.java"

# dto/response/
create_class "$BASE_DIR/dto/response/ApiResponse.java"
create_class "$BASE_DIR/dto/response/AuthResponse.java"
create_class "$BASE_DIR/dto/response/CartResponse.java"
create_class "$BASE_DIR/dto/response/CompanyResponse.java"
create_class "$BASE_DIR/dto/response/OrderResponse.java"
create_class "$BASE_DIR/dto/response/PaymentResponse.java"
create_class "$BASE_DIR/dto/response/ProductResponse.java"
create_class "$BASE_DIR/dto/response/ReviewResponse.java"
create_class "$BASE_DIR/dto/response/SubscriptionResponse.java"
create_class "$BASE_DIR/dto/response/UserResponse.java"

# entity/
create_class "$BASE_DIR/entity/Admin.java"
create_class "$BASE_DIR/entity/Buyer.java"
create_class "$BASE_DIR/entity/BuyerSubscription.java"
create_class "$BASE_DIR/entity/Cart.java"
create_class "$BASE_DIR/entity/CartItem.java"
create_class "$BASE_DIR/entity/Company.java"
create_class "$BASE_DIR/entity/CompanyVerification.java"
create_class "$BASE_DIR/entity/Document.java"
create_class "$BASE_DIR/entity/Order.java"
create_class "$BASE_DIR/entity/OrderItem.java"
create_class "$BASE_DIR/entity/Payment.java"
create_class "$BASE_DIR/entity/Product.java"
create_class "$BASE_DIR/entity/Review.java"
create_class "$BASE_DIR/entity/Seller.java"
create_class "$BASE_DIR/entity/SellerSubscription.java"
create_class "$BASE_DIR/entity/Subscription.java"
create_class "$BASE_DIR/entity/User.java"
create_class "$BASE_DIR/entity/UserVerification.java"

# enums/
create_enum "$BASE_DIR/enums/OrderStatus.java"
create_enum "$BASE_DIR/enums/PaymentMethod.java"
create_enum "$BASE_DIR/enums/PaymentStatus.java"
create_enum "$BASE_DIR/enums/SellerSubscriptionType.java"
create_enum "$BASE_DIR/enums/UserType.java"
create_enum "$BASE_DIR/enums/VerificationStatus.java"

# exception/
create_class "$BASE_DIR/exception/GlobalExceptionHandler.java"
create_class "$BASE_DIR/exception/ResourceNotFoundException.java"
create_class "$BASE_DIR/exception/BadRequestException.java"
create_class "$BASE_DIR/exception/UnauthorizedException.java"
create_class "$BASE_DIR/exception/ForbiddenException.java"

# repository/
create_interface "$BASE_DIR/repository/AdminRepository.java"
create_interface "$BASE_DIR/repository/BuyerRepository.java"
create_interface "$BASE_DIR/repository/BuyerSubscriptionRepository.java"
create_interface "$BASE_DIR/repository/CartItemRepository.java"
create_interface "$BASE_DIR/repository/CartRepository.java"
create_interface "$BASE_DIR/repository/CompanyRepository.java"
create_interface "$BASE_DIR/repository/CompanyVerificationRepository.java"
create_interface "$BASE_DIR/repository/DocumentRepository.java"
create_interface "$BASE_DIR/repository/OrderItemRepository.java"
create_interface "$BASE_DIR/repository/OrderRepository.java"
create_interface "$BASE_DIR/repository/PaymentRepository.java"
create_interface "$BASE_DIR/repository/ProductRepository.java"
create_interface "$BASE_DIR/repository/ReviewRepository.java"
create_interface "$BASE_DIR/repository/SellerRepository.java"
create_interface "$BASE_DIR/repository/SellerSubscriptionRepository.java"
create_interface "$BASE_DIR/repository/SubscriptionRepository.java"
create_interface "$BASE_DIR/repository/UserRepository.java"
create_interface "$BASE_DIR/repository/UserVerificationRepository.java"

# security/
create_class "$BASE_DIR/security/JwtAuthenticationEntryPoint.java"
create_class "$BASE_DIR/security/JwtAuthenticationFilter.java"
create_class "$BASE_DIR/security/JwtTokenProvider.java"
create_class "$BASE_DIR/security/UserDetailsServiceImpl.java"

# service/
create_interface "$BASE_DIR/service/AdminService.java"
create_interface "$BASE_DIR/service/AuthService.java"
create_interface "$BASE_DIR/service/CartService.java"
create_interface "$BASE_DIR/service/CompanyService.java"
create_interface "$BASE_DIR/service/DocumentService.java"
create_interface "$BASE_DIR/service/EmailService.java"
create_interface "$BASE_DIR/service/OrderService.java"
create_interface "$BASE_DIR/service/PaymentService.java"
create_interface "$BASE_DIR/service/ProductService.java"
create_interface "$BASE_DIR/service/ReviewService.java"
create_interface "$BASE_DIR/service/SubscriptionService.java"
create_interface "$BASE_DIR/service/UserService.java"
create_interface "$BASE_DIR/service/VerificationService.java"

# service/impl/
create_class "$BASE_DIR/service/impl/AdminServiceImpl.java"
create_class "$BASE_DIR/service/impl/AuthServiceImpl.java"
create_class "$BASE_DIR/service/impl/CartServiceImpl.java"
create_class "$BASE_DIR/service/impl/CompanyServiceImpl.java"
create_class "$BASE_DIR/service/impl/DocumentServiceImpl.java"
create_class "$BASE_DIR/service/impl/EmailServiceImpl.java"
create_class "$BASE_DIR/service/impl/OrderServiceImpl.java"
create_class "$BASE_DIR/service/impl/PaymentServiceImpl.java"
create_class "$BASE_DIR/service/impl/ProductServiceImpl.java"
create_class "$BASE_DIR/service/impl/ReviewServiceImpl.java"
create_class "$BASE_DIR/service/impl/SubscriptionServiceImpl.java"
create_class "$BASE_DIR/service/impl/UserServiceImpl.java"
create_class "$BASE_DIR/service/impl/VerificationServiceImpl.java"

# util/
create_class "$BASE_DIR/util/AppConstants.java"
create_class "$BASE_DIR/util/FileUploadUtil.java"
create_class "$BASE_DIR/util/ValidationUtil.java"

echo "✅ Files created successfully."