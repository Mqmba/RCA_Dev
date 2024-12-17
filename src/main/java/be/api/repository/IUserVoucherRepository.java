package be.api.repository;

import be.api.model.User;
import be.api.model.UserVoucher;
import be.api.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserVoucherRepository  extends JpaRepository<UserVoucher, Integer> {
    UserVoucher findByVoucher(Voucher voucher);
    List<UserVoucher> findByUser(User user);
    UserVoucher findByUserAndVoucher(User user, Voucher voucher);
}
