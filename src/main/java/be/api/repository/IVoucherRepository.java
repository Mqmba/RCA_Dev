package be.api.repository;

import be.api.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVoucherRepository extends JpaRepository<Voucher, Integer> {
}
