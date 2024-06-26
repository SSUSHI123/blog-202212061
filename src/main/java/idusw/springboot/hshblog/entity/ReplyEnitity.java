package idusw.springboot.hshblog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="reply")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReplyEnitity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // MySQL, MariaDB의 경우 자동증가하는 필드 IDENTITY, Oracle의 경우 SEQUENCE, AUTO 유동적 선택
    private Long idx;
    @Column(length = 200, nullable = false)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private BlogEntity blog;
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity author;
}
