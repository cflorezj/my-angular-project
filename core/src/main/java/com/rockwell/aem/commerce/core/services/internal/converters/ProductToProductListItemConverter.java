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

package com.rockwell.aem.commerce.core.services.internal.converters;

import java.util.Locale;
import java.util.function.Function;

import com.rockwell.aem.commerce.core.models.ProductListItem;
import com.rockwell.aem.commerce.core.models.internal.PriceImpl;
import com.rockwell.aem.commerce.core.models.internal.ProductListItemImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.commerce.core.components.models.common.Price;
import com.adobe.cq.commerce.core.components.services.UrlProvider;
import com.adobe.cq.commerce.magento.graphql.GroupedProduct;
import com.adobe.cq.commerce.magento.graphql.ProductImage;
import com.adobe.cq.commerce.magento.graphql.ProductInterface;
import com.day.cq.wcm.api.Page;

/**
 * Converts a {@link ProductInterface} object into a {@link ProductListItem}.
 */
public class ProductToProductListItemConverter implements Function<ProductInterface, ProductListItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductToProductListItemConverter.class);

    private final Page productPage;
    private final Locale locale;
    private final UrlProvider urlProvider;

    private final SlingHttpServletRequest request;

    public ProductToProductListItemConverter(final Page productPage, final SlingHttpServletRequest request, final UrlProvider urlProvider) {
        this.productPage = productPage;
        this.locale = productPage.getLanguage(false);
        this.request = request;
        this.urlProvider = urlProvider;
    }

    @Override
    public ProductListItem apply(final ProductInterface product) {
        try {
            boolean isStartPrice = product instanceof GroupedProduct;
            Price price = new PriceImpl(product.getPriceRange(), locale, isStartPrice);
            final ProductImage smallImage = product.getSmallImage();

            ProductListItem productListItem = new ProductListItemImpl(product.getSku(),
                product.getUrlKey(),
                product.getName(),
                product.getDescription().getHtml(),
                product.getAsString("lead_time"),
                product.getAsString("life_cycle_status"),
                product.getAsString("repairable"),
                price,
                smallImage == null ? null : smallImage.getUrl(),
                productPage,
                null, // search results aren't targeting specific variant
                request,
                urlProvider);

            return productListItem;
        } catch (Exception e) {
            LOGGER.error("Failed to instantiate product " + product.getSku(), e);
            return null;
        }
    }
}
