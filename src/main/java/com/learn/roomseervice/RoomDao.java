package com.learn.roomseervice;

import com.learn.roomseervice.dto.Rooms;
import com.learn.roomseervice.dto.Rooms;
import com.learn.roomseervice.dto.UserManagement;
import com.learn.roomseervice.dto.UserRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RoomDao  extends JpaRepository<Rooms, Long> {

    @Modifying
    @Query("""

            update Rooms r set r.binStatus = :binStatus, r.binCount = 1 where r.userId = (
           select um.id from UserManagement um where um.phoneNumber IN (:phoneNumber
            ))
          """)
    void updateBinStatus(@Param("binStatus") String binStatus,
                         @Param("phoneNumber") List<String> Phone,
                         @Param("binCount") Integer binCount);

    @Modifying
    @Query("""

            update Rooms r set r.binStatus = :binStatus where r.userId = (
           select um.id from UserManagement um where um.phoneNumber IN (:phoneNumber
            ))
          """)
    void updateBinStatusWithoutBinCount(@Param("binStatus") String binStatus,
                         @Param("phoneNumber") List<String> Phone);

//    @Query("""
//            Select roomMateName  from Rooms  where roomMateName = :roomMateName
//            """)
//    Rooms findNextforBinDuties(@Param("roomMateName") String roomMateName);

    @Query(value = """
            select distinct u.id ,u.username,u.phone_Number,r.room_Number,r.bin_Count,r.common_Area_Clean_Status,r.bin_Status
                 from Rooms r
                 left join User_Management u on
                 u.id = r.user_Id
                 order by r.room_Number asc
            """, nativeQuery = true)
    List<UserRoomInfo> getUsers();
}
