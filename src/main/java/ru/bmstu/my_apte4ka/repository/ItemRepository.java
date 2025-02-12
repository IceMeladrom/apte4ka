package ru.bmstu.my_apte4ka.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bmstu.my_apte4ka.dto.StatisticsItemDto;
import ru.bmstu.my_apte4ka.entity.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Самый продаваемый товар
    @Query("SELECT new ru.bmstu.my_apte4ka.dto.StatisticsItemDto(i, COUNT(i)) " +
            "FROM Receipt r JOIN r.items i " +
            "GROUP BY i.id, i.name, i.price, i.quantity, i.pathToImage " +
            "ORDER BY COUNT(i) DESC")
    List<StatisticsItemDto> findTopSellingItems(Pageable pageable);

    // Самый непродаваемый товар
    @Query("SELECT new ru.bmstu.my_apte4ka.dto.StatisticsItemDto(i, COUNT(i)) " +
            "FROM Receipt r JOIN r.items i " +
            "GROUP BY i.id, i.name, i.price, i.quantity, i.pathToImage " +
            "ORDER BY COUNT(i) ASC")
    List<StatisticsItemDto> findWorstSellingItems(Pageable pageable);

    // Самый дорогой товар
    Optional<Item> findFirstByOrderByPriceDesc();

    // Самый дешевый товар
    Optional<Item> findFirstByOrderByPriceAsc();

    // Наибольшее количество товара
    Optional<Item> findFirstByOrderByQuantityDesc();

    // Наименьшее количество товара
    Optional<Item> findFirstByOrderByQuantityAsc();


}
