package com.example.finalproject.domain.user;

import com.example.finalproject._core.error.exception.Exception400;
import com.example.finalproject._core.error.exception.Exception401;
import com.example.finalproject._core.error.exception.Exception404;
import com.example.finalproject.domain.codi.Codi;
import com.example.finalproject.domain.codi.CodiRepository;
import com.example.finalproject.domain.codi.CodiResponse;
import com.example.finalproject.domain.items.Items;
import com.example.finalproject.domain.items.ItemsRepository;
import com.example.finalproject.domain.items.ItemsResponse;
import com.example.finalproject.domain.order.OrderRepository;
import com.example.finalproject.domain.orderHistory.OrderHistory;
import com.example.finalproject.domain.orderHistory.OrderHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CodiRepository codiRepository;
    private final ItemsRepository itemsRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    //회원가입
    @Transactional
    public User join(UserRequest.JoinDTO reqDTO) {
        Optional<User> userOp = userRepository.findByEmail(reqDTO.getEmail());

        if (userOp.isPresent()){
            throw new Exception400("중복된 이메일이 있습니다.");
        }

        User user = userRepository.save(User.builder()
                .email(reqDTO.getEmail())
                .password(reqDTO.getPassword())
                .nickName(reqDTO.getNickName())
                .status("신청전")
                .blueChecked(false)
                .build());
        return user;
    }

    // 앱 사용자 로그인
    public User login(UserRequest.LoginDTO reqDTO) {
        User user = userRepository.findByEmailAndPassword(reqDTO.getEmail(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("사용자 정보를 찾을 수 없습니다."));
        return user;
    }

    // 앱 세팅 페이지
    public UserResponse.SettingPage settingPage(SessionUser sessionUser) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다."));
        return new UserResponse.SettingPage(user);
    }

    // 앱 사용자 프로필 페이지
    public UserResponse.ProfilePage profilePage(SessionUser sessionUser) {
        User user = userRepository.findByUserIdWithPhoto(sessionUser.getId())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다."));
        return new UserResponse.ProfilePage(user, new UserResponse.ProfilePage.PhotoInfo(user.getPhoto()));
    }

    // 앱 사용자 크리에이터 지원 페이지
    public UserResponse.CreatorApply creatorApplyPage(SessionUser sessionUser) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다."));
        return new UserResponse.CreatorApply(user);
    }

    // 앱 사용자 크리에이터 지원
    @Transactional
    public UserResponse.CreatorApply creatorApply(UserRequest.CreatorApplyDTO reqDTO, SessionUser sessionUser) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다."));

        user.setHeight(reqDTO.getHeight());
        user.setWeight(reqDTO.getWeight());
        user.setInstagram(reqDTO.getInstagram());
        user.setIntroMsg(reqDTO.getComment());
        user.setJob(reqDTO.getJob());
        user.setBlueChecked(false);
        user.setStatus("승인 대기");
        user.setApplyTime(LocalDateTime.now());

        userRepository.save(user);

        return new UserResponse.CreatorApply(user);
    }

    //크리에이터 뷰 페이지
    public UserResponse.CreatorViewDTO creatorView(Integer userId) {
        // 1. 크리에이터 정보 불러오기
        User user = userRepository.findUsersByBlueCheckedAndPhoto(userId)
                .orElseThrow(() -> new Exception401("크리에이터가 아닙니다."));

        // 2. 선택된 크리에이터의 정보와 관련된 코디 목록 가져오기
        List<Codi> codis = codiRepository.findCodiByUserId(userId);

        // 3. 코디에 연결된 아이템 및 포토 정보 가져오기
        List<Items> itemsList = itemsRepository.findItemsByCodiIds(
                codis.stream().map(Codi::getId).collect(Collectors.toList()));

        // 4. DTO로 매핑하기
        List<UserResponse.CodiList> codiDTOs = codis.stream()
                .map(UserResponse.CodiList::new)
                .collect(Collectors.toList());

        List<UserResponse.ItemList> itemDTOs = itemsList.stream()
                .map(UserResponse.ItemList::new)
                .distinct()
                .collect(Collectors.toList());

        UserResponse.CreatorInfo userDTO = new UserResponse.CreatorInfo(user);

        return new UserResponse.CreatorViewDTO(userDTO, codiDTOs, itemDTOs);
    }

    //유저 마이페이지
    public UserResponse.UserMyPage userMyPage(SessionUser sessionUser) {
        // 1. 유저 정보 불러오기
        User user = userRepository.findByUserIdWithPhoto(sessionUser.getId())
                .orElseThrow(() -> new Exception401("인증 되지 않았습니다."));

        // 2. 주문 총 량 찾아오기
        Integer sumOrderItemQty =  orderHistoryRepository.getTotalOrderItemQtyByUserId(Long.valueOf(sessionUser.getId()));

        // 3. UserResponse.UserMyPage 객체 생성 및 반환
        return new UserResponse.UserMyPage(user, sumOrderItemQty);
    }

    //크리에이터 마이페이지
    public UserResponse.CreatorMyPage creatorMyPage(SessionUser sessionUser) {
        // 1. 유저 정보 불러오기
        User user = userRepository.findUsersByBlueCheckedAndPhoto(sessionUser.getId())
                .orElseThrow(() -> new Exception401("인증 되지 않았습니다."));

        // 2. 주문 총 량 찾아오기
        Integer sumOrderItemQty =  orderHistoryRepository.getTotalOrderItemQtyByUserId(Long.valueOf(sessionUser.getId()));

        // 2. 선택된 크리에이터의 정보와 관련된 코디 목록 가져오기
        List<Codi> codis = codiRepository.findCodiByUserId(sessionUser.getId());

        // 3. 코디에 연결된 아이템 및 포토 정보 가져오기
        List<Items> itemsList = itemsRepository.findItemsByCodiIds(
                codis.stream().map(Codi::getId).collect(Collectors.toList()));

        // 4. DTO로 매핑하기
        List<UserResponse.MyCodiList> codiDTOs = codis.stream()
                .map(UserResponse.MyCodiList::new)
                .collect(Collectors.toList());

        List<UserResponse.ItemList> itemDTOs = itemsList.stream()
                .map(UserResponse.ItemList::new)
                .distinct()
                .collect(Collectors.toList());

        UserResponse.CreatorMyInfo creatorInfoDTO = new UserResponse.CreatorMyInfo(user,sumOrderItemQty);

        // 3. UserResponse.UserMyPage 객체 생성 및 반환
        return new UserResponse.CreatorMyPage(creatorInfoDTO,codiDTOs,itemDTOs);
    }

    // 유저 아이템, 코디 통합 검색
    public UserResponse.SearchPage searchPage(String keyword) {
        List<Codi> codiList;
        List<Items> items;
        List<CodiResponse.CodiListDTO> codiListDTO;
        List<ItemsResponse.ItemListDTO> itemListDTO;

        if (keyword == null || keyword.isEmpty()) {
            codiList = codiRepository.findByAllCodi();
            items = itemsRepository.findByAllItems();
            codiListDTO = codiList.stream()
                    .map(CodiResponse.CodiListDTO::new).toList();
            itemListDTO = items.stream()
                    .map(ItemsResponse.ItemListDTO::new).toList();

            return new UserResponse.SearchPage(codiListDTO, itemListDTO);
        }

        items = itemsRepository.findItemsByItemName(keyword);
        codiList = codiRepository.findItemsByCodiTitle(keyword);

        codiListDTO = codiList.stream()
                .map(CodiResponse.CodiListDTO::new).toList();
        itemListDTO = items.stream()
                .map(ItemsResponse.ItemListDTO::new).toList();

        return new UserResponse.SearchPage(codiListDTO, itemListDTO);
    }

}





