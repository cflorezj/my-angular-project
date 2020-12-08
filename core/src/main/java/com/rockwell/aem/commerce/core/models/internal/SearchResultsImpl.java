package com.rockwell.aem.commerce.core.models.internal;

import com.adobe.cq.commerce.core.components.utils.SiteNavigation;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import com.rockwell.aem.commerce.core.models.ProductListItem;
import com.rockwell.aem.commerce.core.models.SearchResults;
import com.rockwell.aem.commerce.core.models.SearchResultsSet;
import com.rockwell.aem.commerce.core.services.SearchResultsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.rockwell.aem.commerce.core.models.internal.SearchOptionsImpl.PAGE_SIZE_DEFAULT;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SearchResults.class,
        resourceType = SearchResultsImpl.RESOURCE_TYPE)
public class SearchResultsImpl implements SearchResults {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsImpl.class);
    static final String RESOURCE_TYPE = "rockwell-aem-commerce/components/commerce/searchresults";

    @Self
    @Via(type = ResourceSuperType.class)
    private com.adobe.cq.commerce.core.components.models.searchresults.SearchResults searchResults;

    @Self
    private SlingHttpServletRequest request;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Style currentStyle;

    @Inject
    private Resource resource;

    @Inject
    protected Page currentPage;

    @Inject
    private SearchResultsService searchResultsService;

    private String searchTerm;
    private SearchOptionsImpl searchOptions;
    private SearchResultsSet searchResultsSet;
    private Page productPage;

    @PostConstruct
    protected void initModel() {
        searchTerm = request.getParameter(SearchOptionsImpl.SEARCH_QUERY_PARAMETER_ID);

        int navPageSize = properties.get(PN_PAGE_SIZE, currentStyle.get(PN_PAGE_SIZE, PAGE_SIZE_DEFAULT));
        productPage = SiteNavigation.getProductPage(currentPage);
        if (productPage == null) {
            productPage = currentPage;
        }

        // make sure the current page from the query string is reasonable i.e. numeric and over 0
        Integer currentPageIndex = calculateCurrentPageCursor(request.getParameter(SearchOptionsImpl.CURRENT_PAGE_PARAMETER_ID));

        Map<String, String> searchFilters = createFilterMap(request.getParameterMap());

        LOGGER.debug("Detected search parameter {}", searchTerm);

        searchOptions = new SearchOptionsImpl();
        searchOptions.setCurrentPage(currentPageIndex);
        searchOptions.setPageSize(navPageSize);
        searchOptions.setAttributeFilters(searchFilters);
        searchOptions.setSearchQuery(searchTerm);
    }

    private Integer calculateCurrentPageCursor(final String currentPageIndexCandidate) {
        // make sure the current page from the query string is reasonable i.e. numeric and over 0
        try {
            int i = Integer.parseInt(currentPageIndexCandidate);
            if (i < 1) {
                i = 1;
            }
            return i;
        } catch (NumberFormatException x) {
            return 1;
        }
    }

    private Map<String, String> createFilterMap(final Map<String, String[]> parameterMap) {
        Map<String, String> filters = new HashMap<>();
        parameterMap.forEach((code, value) -> {
            // we'll make sure there is a value defined for the key
            if (value.length != 1) {
                return;
            }

            filters.put(code, value[0]);
        });

        return filters;
    }

    @Override
    public Collection<ProductListItem> getProducts() {
        if (StringUtils.isBlank(searchTerm)) {
            return Collections.emptyList();
        }

        return getSearchResultsSet().getProductListItems();
    }

    @Override
    public SearchResultsSet getSearchResultsSet() {
        if (searchResultsSet == null) {
            searchResultsSet = searchResultsService.performSearch(searchOptions, resource, productPage, request);
        }
        return searchResultsSet;
    }

    @Override
    public boolean loadClientPrice() {
        return searchResults.loadClientPrice();
    }
}
