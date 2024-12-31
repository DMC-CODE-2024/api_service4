/*
package com.ceir.CeirCode.repo.app;

import com.ceir.CeirCode.model.app.Feature;
import com.ceir.CeirCode.response.FeatureListModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserToStakehoderfeatureMappingRepo extends JpaRepository<Feature, Long>, JpaSpecificationExecutor<Feature> {
    @Query("SELECT new com.ceir.CeirCode.response.FeatureListModel(f.id,f.featureName,l.url,l.iframeUrl, f.logo) FROM  com.ceir.CeirCode.model.app.Feature f, com.ceir.CeirCode.model.app.GroupFeature gf, com.ceir.CeirCode.model.app.UserGroupMembership ugm, com.ceir.CeirCode.model.app.Link l WHERE ugm.userId = :userId  AND ugm.groupId = gf.groupId AND gf.featureId = f.id  AND f.link = l.linkName AND gf.status='3' AND f.status='3' AND ugm.status='3' order by gf.displayOrder")
    public List<FeatureListModel> getFeature(long userId);


    @Query(value = "select module_tag_name from module_tag where id in(select module_tag_id from module where status=3 and id in(select module_id from role_feature_module_access where status=3 and role_id in(select role_id from group_role where status=3 and group_id in(select group_id from user_group_membership where status=3 and user_id= :userId)) and status=3 and feature_id= :featureId));", nativeQuery = true)
    public List<String> getFeatureIcon(@Param("userId") long userId, @Param("featureId") long featureId);
}
*/
