package com.sneakershop.kiva.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.sneakershop.kiva.entity.*;
import com.sneakershop.kiva.exception.BadRequestException;
import com.sneakershop.kiva.exception.InternalServerException;
import com.sneakershop.kiva.exception.NotFoundException;
import com.sneakershop.kiva.model.dto.OrderDetailDTO;
import com.sneakershop.kiva.model.dto.OrderInfoDTO;
import com.sneakershop.kiva.model.request.CreateOrderRequest;
import com.sneakershop.kiva.model.request.UpdateDetailOrder;
import com.sneakershop.kiva.model.request.UpdateStatusOrderRequest;
import com.sneakershop.kiva.repository.OrderRepository;
import com.sneakershop.kiva.repository.ProductRepository;
import com.sneakershop.kiva.repository.ProductSizeRepository;
import com.sneakershop.kiva.repository.StatisticRepository;
import com.sneakershop.kiva.service.OrderService;
import com.sneakershop.kiva.service.PromotionService;

import static com.sneakershop.kiva.config.Contant.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private StatisticRepository statisticRepository;

    @Override
    public Page<Order> adminGetListOrders(String id, String name, String phone, String status, String product, int page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        int limit = 10;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
        return orderRepository.adminGetListOrder(id, name, phone, status, product, pageable);
    }

    @Override
    public Order createOrder(CreateOrderRequest createOrderRequest, long userId) {

        //Ki???m tra s???n ph???m c?? t???n t???i
        Optional<Product> product = productRepository.findById(createOrderRequest.getProductId());
        if (product.isEmpty()) {
            throw new NotFoundException("S???n ph???m kh??ng t???n t???i!");
        }

        //Ki???m tra size c?? s???n
        ProductSize productSize = productSizeRepository.checkProductAndSizeAvailable(createOrderRequest.getProductId(), createOrderRequest.getSize());
        if (productSize == null) {
            throw new BadRequestException("Size gi??y s???n ph???m t???m h???t, Vui l??ng ch???n s???n ph???m kh??c!");
        }

        //Ki???m tra gi?? s???n ph???m
        if (product.get().getSalePrice() != createOrderRequest.getProductPrice()) {
            throw new BadRequestException("Gi?? s???n ph???m thay ?????i, Vui l??ng ?????t h??ng l???i!");
        }
        Order order = new Order();
        User user = new User();
        user.setId(userId);
        order.setCreatedBy(user);
        order.setBuyer(user);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setReceiverAddress(createOrderRequest.getReceiverAddress());
        order.setReceiverName(createOrderRequest.getReceiverName());
        order.setReceiverPhone(createOrderRequest.getReceiverPhone());
        order.setNote(createOrderRequest.getNote());
        order.setSize(createOrderRequest.getSize());
        order.setPrice(createOrderRequest.getProductPrice());
        order.setTotalPrice(createOrderRequest.getTotalPrice());
        order.setStatus(ORDER_STATUS);
        order.setQuantity(1);
        order.setProduct(product.get());

        orderRepository.save(order);
        return order;

    }

    @Override
    public void updateDetailOrder(UpdateDetailOrder updateDetailOrder, long id, long userId) {
        //Ki???m tr??? c?? ????n h??ng
        Optional<Order> rs = orderRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("????n h??ng kh??ng t???n t???i");
        }

        Order order = rs.get();
        //Ki???m tra tr???ng th??i ????n h??ng
        if (order.getStatus() != ORDER_STATUS) {
            throw new BadRequestException("Ch??? c???p nh???t ????n h??ng ??? tr???ng th??i ch??? l???y h??ng");
        }

        //Ki???m tra size s???n ph???m
        Optional<Product> product = productRepository.findById(updateDetailOrder.getProductId());
        if (product.isEmpty()) {
            throw new BadRequestException("S???n ph???m kh??ng t???n t???i");
        }
        //Ki???m tra gi??
        if (product.get().getSalePrice() != updateDetailOrder.getProductPrice()) {
            throw new BadRequestException("Gi?? s???n ph???m thay ?????i vui l??ng ?????t h??ng l???i");
        }

        ProductSize productSize = productSizeRepository.checkProductAndSizeAvailable(updateDetailOrder.getProductId(), updateDetailOrder.getSize());
        if (productSize == null) {
            throw new BadRequestException("Size gi??y s???n ph???m t???m h???t, Vui l??ng ch???n s???n ph???m kh??c");
        }

        //Ki???m tra khuy???n m???i
        if (updateDetailOrder.getCouponCode() != "") {
            Promotion promotion = promotionService.checkPromotion(updateDetailOrder.getCouponCode());
            if (promotion == null) {
                throw new NotFoundException("M?? khuy???n m??i kh??ng t???n t???i ho???c ch??a ???????c k??ch ho???t");
            }
            long promotionPrice = promotionService.calculatePromotionPrice(updateDetailOrder.getProductPrice(), promotion);
            if (promotionPrice != updateDetailOrder.getTotalPrice()) {
                throw new BadRequestException("T???ng gi?? tr??? ????n h??ng thay ?????i. Vui l??ng ki???m tra v?? ?????t l???i ????n h??ng");
            }
            Order.UsedPromotion usedPromotion = new Order.UsedPromotion(updateDetailOrder.getCouponCode(), promotion.getDiscountType(), promotion.getDiscountValue(), promotion.getMaximumDiscountValue());
            order.setPromotion(usedPromotion);
        }

        order.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        order.setProduct(product.get());
        order.setSize(updateDetailOrder.getSize());
        order.setPrice(updateDetailOrder.getProductPrice());
        order.setTotalPrice(updateDetailOrder.getTotalPrice());


        order.setStatus(ORDER_STATUS);
        User user = new User();
        user.setId(userId);
        order.setModifiedBy(user);
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerException("L???i khi c???p nh???t");
        }
    }


    @Override
    public Order findOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new NotFoundException("????n h??ng kh??ng t???n t???i");
        }
        return order.get();
    }

    @Override
    public void updateStatusOrder(UpdateStatusOrderRequest updateStatusOrderRequest, long orderId, long userId) {
        Optional<Order> rs = orderRepository.findById(orderId);
        if (rs.isEmpty()) {
            throw new NotFoundException("????n h??ng kh??ng t???n t???i");
        }
        Order order = rs.get();
        //Ki???m tra tr???ng th??i c???a ????n h??ng
        boolean check = false;
        for (Integer status : LIST_ORDER_STATUS) {
            if (status == updateStatusOrderRequest.getStatus()) {
                check = true;
                break;
            }
        }
        if (!check) {
            throw new BadRequestException("Tr???ng th??i ????n h??ng kh??ng h???p l???");
        }
        //C???p nh???t tr???ng th??i ????n h??ng
        if (order.getStatus() == ORDER_STATUS) {
            //????n h??ng ??? tr???ng th??i ch??? l???y h??ng
            if (updateStatusOrderRequest.getStatus() == ORDER_STATUS) {
                order.setReceiverPhone(updateStatusOrderRequest.getReceiverPhone());
                order.setReceiverName(updateStatusOrderRequest.getReceiverName());
                order.setReceiverAddress(updateStatusOrderRequest.getReceiverAddress());
                //????n h??ng ??? tr???ng th??i ??ang v???n chuy???n
            } else if (updateStatusOrderRequest.getStatus() == DELIVERY_STATUS) {
                //Tr??? ??i m???t s???n ph???m
                productSizeRepository.minusOneProductBySize(order.getProduct().getId(), order.getSize());
                //????n h??ng ??? tr???ng th??i ???? giao h??ng
            } else if (updateStatusOrderRequest.getStatus() == COMPLETED_STATUS) {
                //Tr??? ??i m???t s???n ph???m v?? c???ng m???t s???n ph???m v??o s???n ph???m ???? b??n v?? c???ng ti???n
                productSizeRepository.minusOneProductBySize(order.getProduct().getId(), order.getSize());
                productRepository.plusOneProductTotalSold(order.getProduct().getId());
                statistic(order.getTotalPrice(), order.getQuantity(), order);
            } else if (updateStatusOrderRequest.getStatus() != CANCELED_STATUS) {
                throw new BadRequestException("Kh??ng th??? chuy???n sang tr???ng th??i n??y");
            }
            //????n h??ng ??? tr???ng th??i ??ang giao h??ng
        } else if (order.getStatus() == DELIVERY_STATUS) {
            //????n h??ng ??? tr???ng th??i ???? giao h??ng
            if (updateStatusOrderRequest.getStatus() == COMPLETED_STATUS) {
                //C???ng m???t s???n ph???m v??o s???n ph???m ???? b??n v?? c???ng ti???n
                productRepository.plusOneProductTotalSold(order.getProduct().getId());
                statistic(order.getTotalPrice(), order.getQuantity(), order);
                //????n h??ng ??? tr???ng th??i ???? h???y
            } else if (updateStatusOrderRequest.getStatus() == RETURNED_STATUS) {
                //C???ng l???i m???t s???n ph???m ???? b??? tr???
                productSizeRepository.plusOneProductBySize(order.getProduct().getId(), order.getSize());
                //????n h??ng ??? tr???ng th??i ???? tr??? h??ng
            } else if (updateStatusOrderRequest.getStatus() == CANCELED_STATUS) {
                //C???ng l???i m???t s???n ph???m ???? b??? tr???
                productSizeRepository.plusOneProductBySize(order.getProduct().getId(), order.getSize());
            } else if (updateStatusOrderRequest.getStatus() != DELIVERY_STATUS) {
                throw new BadRequestException("Kh??ng th??? chuy???n sang tr???ng th??i n??y");
            }
            //????n h??ng ??? tr???ng th??i ???? giao h??ng
        } else if (order.getStatus() == COMPLETED_STATUS) {
            //????n h??ng ??ang ??? tr???ng th??i ???? h???y
            if (updateStatusOrderRequest.getStatus() == RETURNED_STATUS) {
                //C???ng m???t s???n ph???m ???? b??? tr??? v?? tr??? ??i m???t s???n ph???m ???? b??n v?? tr??? s??? ti???n
                productSizeRepository.plusOneProductBySize(order.getProduct().getId(), order.getSize());
                productRepository.minusOneProductTotalSold(order.getProduct().getId());
                updateStatistic(order.getTotalPrice(), order.getQuantity(), order);
            } else if (updateStatusOrderRequest.getStatus() != COMPLETED_STATUS) {
                throw new BadRequestException("Kh??ng th??? chuy???n sang tr???ng th??i n??y");
            }
        } else {
            if (order.getStatus() != updateStatusOrderRequest.getStatus()) {
                throw new BadRequestException("Kh??ng th??? chuy???n ????n h??ng sang tr???ng th??i n??y");
            }
        }

        User user = new User();
        user.setId(userId);
        order.setModifiedBy(user);
        order.setModifiedAt(new Timestamp(System.currentTimeMillis()));
        order.setNote(updateStatusOrderRequest.getNote());
        order.setStatus(updateStatusOrderRequest.getStatus());
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerException("L???i khi c???p nh???t tr???ng th??i");
        }
    }

    @Override
    public List<OrderInfoDTO> getListOrderOfPersonByStatus(int status, long userId) {
        List<OrderInfoDTO> list = orderRepository.getListOrderOfPersonByStatus(status, userId);

        for (OrderInfoDTO dto : list) {
            for (int i = 0; i < SIZE_VN.size(); i++) {
                if (SIZE_VN.get(i) == dto.getSizeVn()) {
                    dto.setSizeUs(SIZE_US[i]);
                    dto.setSizeCm(SIZE_CM[i]);
                }
            }
        }
        return list;
    }

    @Override
    public OrderDetailDTO userGetDetailById(long id, long userId) {
        OrderDetailDTO order = orderRepository.userGetDetailById(id, userId);
        if (order == null) {
            return null;
        }

        if (order.getStatus() == ORDER_STATUS) {
            order.setStatusText("Ch??? l???y h??ng");
        } else if (order.getStatus() == DELIVERY_STATUS) {
            order.setStatusText("??ang giao h??ng");
        } else if (order.getStatus() == COMPLETED_STATUS) {
            order.setStatusText("???? giao h??ng");
        } else if (order.getStatus() == CANCELED_STATUS) {
            order.setStatusText("????n h??ng ???? tr??? l???i");
        } else if (order.getStatus() == RETURNED_STATUS) {
            order.setStatusText("????n h??ng ???? h???y");
        }

        for (int i = 0; i < SIZE_VN.size(); i++) {
            if (SIZE_VN.get(i) == order.getSizeVn()) {
                order.setSizeUs(SIZE_US[i]);
                order.setSizeCm(SIZE_CM[i]);
            }
        }

        return order;
    }

    @Override
    public void userCancelOrder(long id, long userId) {
        Optional<Order> rs = orderRepository.findById(id);
        if (rs.isEmpty()) {
            throw new NotFoundException("????n h??ng kh??ng t???n t???i");
        }
        Order order = rs.get();
        if (order.getBuyer().getId() != userId) {
            throw new BadRequestException("B???n kh??ng ph???i ch??? nh??n ????n h??ng");
        }
        if (order.getStatus() != ORDER_STATUS) {
            throw new BadRequestException("Tr???ng th??i ????n h??ng kh??ng ph?? h???p ????? h???y. Vui l??ng li??n h??? v???i shop ????? ???????c h??? tr???");
        }

        order.setStatus(CANCELED_STATUS);
        orderRepository.save(order);
    }

    @Override
    public long getCountOrder() {
        return orderRepository.count();
    }

    public void statistic(long amount, int quantity, Order order) {
        Statistic statistic = statisticRepository.findByCreatedAT();
        if (statistic != null){
            statistic.setOrder(order);
            statistic.setSales(statistic.getSales() + amount);
            statistic.setQuantity(statistic.getQuantity() + quantity);
            statistic.setProfit(statistic.getSales() - (statistic.getQuantity() * order.getProduct().getPrice()));
            statisticRepository.save(statistic);
        }else {
            Statistic statistic1 = new Statistic();
            statistic1.setOrder(order);
            statistic1.setSales(amount);
            statistic1.setQuantity(quantity);
            statistic1.setProfit(amount - (quantity * order.getProduct().getPrice()));
            statistic1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            statisticRepository.save(statistic1);
        }
    }

    public void updateStatistic(long amount, int quantity, Order order) {
        Statistic statistic = statisticRepository.findByCreatedAT();
        if (statistic != null) {
            statistic.setOrder(order);
            statistic.setSales(statistic.getSales() - amount);
            statistic.setQuantity(statistic.getQuantity() - quantity);
            statistic.setProfit(statistic.getSales() - (statistic.getQuantity() * order.getProduct().getPrice()));
            statisticRepository.save(statistic);
        }
    }
}
