/*
package com.ceir.CeirCode.service;

import com.ceir.CeirCode.Constants.Datatype;
import com.ceir.CeirCode.Constants.SearchOperation;
import com.ceir.CeirCode.SpecificationBuilder.GenericSpecificationBuilder;
import com.ceir.CeirCode.configuration.PropertiesReaders;

import com.ceir.CeirCode.repo.app.DashboardUsersFeatureStateMapRepository;
import com.ceir.CeirCode.repo.app.SystemConfigDbListRepository;
import com.ceir.CeirCode.repo.app.UserProfileRepo;
import com.ceir.CeirCode.repoService.SystemConfigDbRepoService;
import com.ceir.CeirCode.repoService.UserRepoService;
import com.ceir.CeirCode.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class UserProfileService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    UserProfileRepo userProfileRepo;

    @Autowired
    PropertiesReaders propertiesReader;

    @Autowired
    SystemConfigDbListRepository systemConfigRepo;

    @Autowired
    SystemConfigDbRepoService systemConfigurationDbRepoImpl;

    @Autowired
    UserService userService;

    @Autowired
    UserRepoService userRepoService;

    @Autowired
    Utility utility;

    @Autowired
    DashboardUsersFeatureStateMapRepository dashboardUsersFeatureStateMapRepository;


    private GenericSpecificationBuilder<UserProfile> buildSpecification(FilterRequest filterRequest, List<StateMgmtDb> statusList, String source) {

        GenericSpecificationBuilder<UserProfile> uPSB = new GenericSpecificationBuilder<UserProfile>(propertiesReader.dialect);
        User user = userRepoService.findByUSerId(filterRequest.getUserId());


        if (Objects.nonNull(filterRequest.getStartDate()) && filterRequest.getStartDate() != "")
            uPSB.addSpecification(uPSB.joinWithUser(new SearchCriteria("createdOn", filterRequest.getStartDate(), SearchOperation.GREATER_THAN, Datatype.DATE)));

        if (Objects.nonNull(filterRequest.getEndDate()) && filterRequest.getEndDate() != "")
            uPSB.addSpecification(uPSB.joinWithUser(new SearchCriteria("createdOn", filterRequest.getEndDate(), SearchOperation.LESS_THAN, Datatype.DATE)));

        if (Objects.nonNull(filterRequest.getAsType()) && filterRequest.getAsType() != -1)
            uPSB.with(new SearchCriteria("type", filterRequest.getAsType(), SearchOperation.EQUALITY, Datatype.INTEGER));

        if (Objects.nonNull(filterRequest.getEmail()) && !filterRequest.getEmail().isEmpty())
            uPSB.with(new SearchCriteria("email", filterRequest.getEmail(), SearchOperation.LIKE, Datatype.STRING));

        if (Objects.nonNull(filterRequest.getPhoneNo()) && !filterRequest.getPhoneNo().isEmpty())
            uPSB.with(new SearchCriteria("phoneNo", filterRequest.getPhoneNo(), SearchOperation.LIKE, Datatype.STRING));
//		
//		uPSB.addSpecification((Specification<UserProfile>)(root, query, cb)-> {
//			return cb.equal(root.join("user").get("username"), filterRequest.getPhoneNo());
//			});


        if (Objects.nonNull(filterRequest.getFilteredUsername()) && filterRequest.getFilteredUsername() != "") {
            log.info("username in filterRequest::::::::" + filterRequest.getFilteredUsername());
            uPSB.addSpecification(uPSB.joinWithUser(new SearchCriteria("user-username", filterRequest.getFilteredUsername(), SearchOperation.LIKE, Datatype.STRING)));
        }

        //if(Objects.nonNull(filterRequest.getUsername()) && !filterRequest.getUsername().isEmpty()) {

        //uPSB.addSpecification(uPSB.joinWithUser(new SearchCriteria("user-username",filterRequest.getUsername(), SearchOperation.LIKE, Datatype.STRING)));
        //}
        else if (Objects.nonNull(filterRequest.getStatus()) && filterRequest.getStatus() != -1) {
            log.info("status in filterRequest::::::::" + filterRequest.getStatus());
            uPSB.addSpecification(uPSB.joinWithUser(new SearchCriteria("currentStatus", filterRequest.getStatus(), SearchOperation.EQUALITY, Datatype.INTEGER)));
        } else {
            //uPSB.addSpecification(uPSB.joinWithUser(new SearchCriteria("currentStatus",UserStatus.PENDING_ADMIN_APPROVAL.getCode(), SearchOperation.EQUALITY, Datatype.INT)));
            if (Objects.nonNull(filterRequest.getFeatureId()) && Objects.nonNull(filterRequest.getUserTypeId())) {

                List<DashboardUsersFeatureStateMap> dashboardUsersFeatureStateMap = dashboardUsersFeatureStateMapRepository.findByUserTypeIdAndFeatureId(filterRequest.getUserTypeId(), filterRequest.getFeatureId());
                log.info("dashboard data size: " + dashboardUsersFeatureStateMap.size());

                List<Integer> stockStatus = new LinkedList<>();

                if (Objects.nonNull(dashboardUsersFeatureStateMap)) {
                    if ("dashboard".equalsIgnoreCase(source) || "menu".equalsIgnoreCase(source)) {
                        log.info("if filter is dashboard or menu");
                        for (DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMap) {
                            stockStatus.add(dashboardUsersFeatureStateMap2.getState());
                        }
                    } else if ("filter".equalsIgnoreCase(source)) {
                        log.info("if source is filter");
                        if (nothingInFilter(filterRequest)) {
                            log.info("if filters are empty");
                            for (DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMap) {
                                stockStatus.add(dashboardUsersFeatureStateMap2.getState());
                            }
                        } else {
                            log.info("if filters are not  empty");
                            for (StateMgmtDb stateMgmtDb : statusList) {
                                stockStatus.add(stateMgmtDb.getState());
                            }
                        }
                    } else if ("noti".equalsIgnoreCase(source)) {

                        uPSB.addSpecification(uPSB.joinWithUser(new SearchCriteria("username", filterRequest.getUsername(), SearchOperation.EQUALITY, Datatype.STRING)));

                        //log.info("Skip status check, because source is noti."+user.getUsername());
                    }

                    log.info("Array list to add is = " + stockStatus);
                    if (!stockStatus.isEmpty()) {
                        //specificationBuilder.addSpecification(specificationBuilder.in("stockStatus", stockStatus));
                        uPSB.addSpecification(uPSB.inQuery("currentStatus", stockStatus));

                    } else {
                        log.warn("no predefined status are available.");
                    }
                }
            }

        }


        if (Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()) {
            //uPSB.orSearchUser(new SearchCriteria("username", filterRequest.getSearchString(), SearchOperation.EQUALITY_CASE_INSENSITIVE, Datatype.STRING));
            // uPSB.orSearchUsertype(new SearchCriteria("usertypeName", filterRequest.getSearchString(), SearchOperation.EQUALITY_CASE_INSENSITIVE, Datatype.STRING));
            uPSB.orSearch(new SearchCriteria("user-username", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
            uPSB.orSearch(new SearchCriteria("user-usertype-usertypeName", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));

            uPSB.orSearch(new SearchCriteria("createdOn", filterRequest.getSearchString(), SearchOperation.EQUALITY, Datatype.DATE));
            uPSB.orSearch(new SearchCriteria("modifiedOn", filterRequest.getSearchString(), SearchOperation.EQUALITY, Datatype.DATE));
            //  uPSB.orSearch(new SearchCriteria("type", filterRequest.getSearchString(),SearchOperation.LIKE, Datatype.STRING));
            uPSB.orSearch(new SearchCriteria("email", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
            uPSB.orSearch(new SearchCriteria("phoneNo", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));

        }

        return uPSB;
    }

    public boolean nothingInFilter(FilterRequest filterRequest) {
        if (Objects.nonNull(filterRequest.getStartDate()) && filterRequest.getStartDate() != "") {
            return Boolean.FALSE;
        }
        if (Objects.nonNull(filterRequest.getEndDate()) && filterRequest.getEndDate() != "") {
            return Boolean.FALSE;
        }

        if (Objects.nonNull(filterRequest.getStatus()) && filterRequest.getStatus() != -1) {
            return Boolean.FALSE;
        }

        if (Objects.nonNull(filterRequest.getAsType()) && filterRequest.getAsType() != -1) {
            return Boolean.FALSE;
        }
        if (Objects.nonNull(filterRequest.getUserRoleTypeId()) && filterRequest.getUserRoleTypeId() != -1) {
            return Boolean.FALSE;
        }

        if (Objects.nonNull(filterRequest.getEmail()) && !filterRequest.getEmail().isEmpty()) {
            return Boolean.FALSE;
        }

        if (Objects.nonNull(filterRequest.getPhoneNo()) && !filterRequest.getPhoneNo().isEmpty()) {
            return Boolean.FALSE;
        }

        if (Objects.nonNull(filterRequest.getUsername()) && !filterRequest.getUsername().isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

*/
/*    public List<UserProfile> getAll(FilterRequest filterRequest, String source) {
        List<StateMgmtDb> statusList = null;
        try {

            statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());

            List<UserProfile> systemConfigListDbs = userProfileRepo.findAll(buildSpecification(filterRequest, statusList, source).build(), Sort.by(Sort.Direction.DESC, "modifiedOn"));
            userService.saveUserTrail(filterRequest.getUserId(), filterRequest.getUsername(),
                    filterRequest.getUserType(), filterRequest.getUserTypeId(), Features.Registration_Request, SubFeatures.EXPORT, filterRequest.getFeatureId(),
                    filterRequest.getPublicIp(), filterRequest.getBrowser());
            for (UserProfile userProfile : systemConfigListDbs) {
                log.info("after fetching systemConfigListDbs data" + systemConfigListDbs);
                for (StateMgmtDb stateMgmtDb : statusList) {
                    if (userProfile.getUser().getCurrentStatus() == stateMgmtDb.getState()) {
                        userProfile.getUser().setStateInterp(stateMgmtDb.getInterp());
                        log.info("stateMgmtDb.getInterp() data" + stateMgmtDb.getInterp());
                        break;
                    }
                }
            }
            log.info("systemConfigListDbs:::::::::" + systemConfigListDbs);
            return systemConfigListDbs;

        } catch (Exception e) {
            log.info("Exception found =" + e.getMessage());
            log.info(e.getClass().getMethods().toString());
            log.info(e.toString());
            return null;
        }

    }*//*



*/
/*    public Page<UserProfile> viewAllRecord(FilterRequest filterRequest, Integer pageNo, Integer pageSize, String source) {
        try {
            log.info("filter data:  " + filterRequest);
            //	Pageable pageable = PageRequest.of(pageNo, pageSize,Sort.by(Sort.Direction.DESC, "modifiedOn"));
            String orderColumn = "Created On".equalsIgnoreCase(filterRequest.getColumnName()) ? "user.createdOn"
                    : "Modified On".equalsIgnoreCase(filterRequest.getColumnName()) ? "user.modifiedOn"
                    : "User ID".equalsIgnoreCase(filterRequest.getColumnName()) ? "user.username"
                    : "Email".equalsIgnoreCase(filterRequest.getColumnName()) ? "email"
                    : "Phone No.".equalsIgnoreCase(filterRequest.getColumnName()) ? "phoneNo"
                    : "Type".equalsIgnoreCase(filterRequest.getColumnName())
                    ? "type"
                    : "User Type".equalsIgnoreCase(filterRequest.getColumnName())
                    ? "user.usertype.usertypeName"
                    : "Status".equalsIgnoreCase(filterRequest.getColumnName())
                    ? "user.currentStatus" : "modifiedOn";
            Sort.Direction direction;
			*//*
*/
/*if("modifiedOn".equalsIgnoreCase(orderColumn)) {
				direction=Sort.Direction.DESC;
			}
			else {*//*
*/
/*
            direction = SortDirection.getSortDirection(filterRequest.getSort());
            *//*
*/
/* } *//*
*/
/*
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, orderColumn));


            List<StateMgmtDb> statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
            Page<UserProfile> page = userProfileRepo.findAll(buildSpecification(filterRequest, statusList, source).build(), pageable);

            if ("filter".equals(source)) {
                userService.saveUserTrail(filterRequest.getUserId(), filterRequest.getUsername(),
                        filterRequest.getUserType(), filterRequest.getUserTypeId(), Features.Registration_Request, SubFeatures.FILTER, filterRequest.getFeatureId(), filterRequest.getPublicIp(), filterRequest.getBrowser());
            } else {
                userService.saveUserTrail(filterRequest.getUserId(), filterRequest.getUsername(),
                        filterRequest.getUserType(), filterRequest.getUserTypeId(), Features.Registration_Request, SubFeatures.VIEW_ALL, filterRequest.getFeatureId(), filterRequest.getPublicIp(), filterRequest.getBrowser());
            }
            for (UserProfile userProfile : page.getContent()) {
                log.info("after fetching state mgmt data");

                for (StateMgmtDb stateMgmtDb : statusList) {
                    if (userProfile.getUser().getCurrentStatus() == stateMgmtDb.getState()) {
                        userProfile.getUser().setStateInterp(stateMgmtDb.getInterp());
                        break;
                    }
                }
                // setInterp(consignmentMgmt2);
            }
            return page;

        } catch (Exception e) {
            log.info("Exception found =" + e.getMessage());
            log.info(e.getClass().getMethods().toString());
            log.info(e.toString());
            return null;

        }
    }*//*

}
*/
