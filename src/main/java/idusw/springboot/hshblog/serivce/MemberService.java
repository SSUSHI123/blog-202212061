package idusw.springboot.hshblog.serivce;

import idusw.springboot.hshblog.entity.MemberEntity;
import idusw.springboot.hshblog.model.MemberDto;

import java.util.List;

public interface MemberService {
    int create(MemberDto memberDto);
    MemberDto readByIdx(Long idx);
    List<MemberDto> readAll();
    int update(MemberDto memberDto);
    int delete(MemberDto memberDto);

    MemberDto loginById(MemberDto memberDto); // id / pw 활용

    // Conversion
    default MemberEntity dtoToEntity(MemberDto memberDto) {
        MemberEntity entity = MemberEntity.builder()
                .idx(memberDto.getIdx())
                .id(memberDto.getId())
                .pw(memberDto.getPw())
                .name(memberDto.getName())
                .phone(memberDto.getPhone())
                .address(memberDto.getAddress())
                .email(memberDto.getEmail())
                .build();
        return entity;
    }
    
    default MemberDto entityToDto(MemberEntity entity) {
        MemberDto memberDto = MemberDto.builder()
                .idx(entity.getIdx())
                .id(entity.getId())
                .pw(entity.getPw())
                .name(entity.getName())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .email(entity.getEmail())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return memberDto;
    }
}
