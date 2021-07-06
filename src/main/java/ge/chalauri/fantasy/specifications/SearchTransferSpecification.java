package ge.chalauri.fantasy.specifications;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import ge.chalauri.fantasy.model.Country;
import ge.chalauri.fantasy.model.Player;
import ge.chalauri.fantasy.model.Team;
import ge.chalauri.fantasy.model.Transfer;
import ge.chalauri.fantasy.model.request.TransferSearch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class SearchTransferSpecification {

    public static Specification<Transfer> create(TransferSearch transferSearch){
        return (Specification<Transfer>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            root.fetch("fromTeam");
            root.fetch("player");
            Join<Transfer, Team> fromTeamJoin = root.join("fromTeam", JoinType.INNER);
            Join<Transfer, Player> playerJoin = root.join("player", JoinType.INNER);
            Join<Player, Country> countryJoin = playerJoin.join("country", JoinType.INNER);

            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("bought"), false)));

            if (StringUtils.isNotBlank(transferSearch.getTeamName())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(fromTeamJoin.get("name"), transferSearch.getTeamName())));
            }

            if (StringUtils.isNotBlank(transferSearch.getCountry())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(countryJoin.get("countryName"), transferSearch.getCountry())));
            }

            if (StringUtils.isNotBlank(transferSearch.getPlayerName())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(playerJoin.get("firstName"), transferSearch.getPlayerName())));
            }

            if (transferSearch.getValue() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("transferPrice"), transferSearch.getValue())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}
