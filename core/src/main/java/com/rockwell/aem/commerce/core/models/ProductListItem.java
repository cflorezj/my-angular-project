package com.rockwell.aem.commerce.core.models;

public interface ProductListItem extends com.adobe.cq.commerce.core.components.models.common.ProductListItem {
    String getLeadTime();

    String getLifeCycleStatus();

    String getRepairable();
}
