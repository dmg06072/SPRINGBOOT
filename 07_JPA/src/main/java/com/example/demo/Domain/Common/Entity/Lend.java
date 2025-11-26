package com.example.demo.Domain.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="lend")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name="username",
            foreignKey =@ForeignKey(
                    name="FK_LEND_USER",
                    foreignKeyDefinition = "FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE"
            )
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name="bookCode",
            foreignKey = @ForeignKey(name="FK_LEND_BOOK",
                foreignKeyDefinition = "FOREIGN KEY (bookCode) REFERENCES book(bookCode) ON DELETE CASCADE ON UPDATE CASCADE"
            )
    )
    private Book book;
}
