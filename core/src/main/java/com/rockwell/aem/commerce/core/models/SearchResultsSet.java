/*******************************************************************************
 *
 *    Copyright 2019 Adobe. All rights reserved.
 *    This file is licensed to you under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License. You may obtain a copy
 *    of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software distributed under
 *    the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 *    OF ANY KIND, either express or implied. See the License for the specific language
 *    governing permissions and limitations under the License.
 *
 ******************************************************************************/

package com.rockwell.aem.commerce.core.models;

import java.util.List;
import java.util.Map;

import com.adobe.cq.commerce.core.search.models.Pager;
import com.adobe.cq.commerce.core.search.models.SearchAggregation;
import com.adobe.cq.commerce.core.search.models.SearchOptions;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Represents a set of search results from a backend service. This would generally contain the actual {@link ProductListItem}s
 * as well as {@link SearchAggregation}s.
 */
@ConsumerType
public interface SearchResultsSet {

    /**
     * Get the search options used to provide this search result set.
     *
     * @return the {@link SearchOptions} used in the search
     */
    SearchOptions getSearchOptions();

    /**
     * Get the total number of results.
     *
     * @return the total number of results
     */
    Integer getTotalResults();

    /**
     * Get a map of the applied search query string parameters.
     *
     * @return the query string parameters (key value pairs)
     */
    Map<String, String> getAppliedQueryParameters();

    /**
     * Get the result product list items. These are the actual result of the search.
     *
     * @return the resulting products
     */
    List<ProductListItem> getProductListItems();

    /**
     * Get the available search aggregations for the given result set.
     *
     * @return the aggregations or filters available for the search result set.
     */
    List<SearchAggregation> getSearchAggregations();

    /**
     * Get the list of aggregations or filters that were applied in this search.
     *
     * @return the applied filter aggregations
     */
    List<SearchAggregation> getAppliedAggregations();

    /**
     * Get the available search aggregations for the given result set
     *
     * @return the available aggregations
     */
    List<SearchAggregation> getAvailableAggregations();

    /**
     * Returns the pager model, responsible for handling the pagination of search results.
     * 
     * @return
     */
    Pager getPager();

    /**
     * @return {@code true} if the result set provides search aggregations for faceted search support, {@code false} otherwise
     */
    boolean hasAggregations();

    /**
     * @return {@code true} if the result set provides pagination, {@code false} otherwise
     */
    boolean hasPagination();
}
