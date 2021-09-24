package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.modelws.response.PaginationResponse;

import javax.ejb.Stateless;

/**
 * Created by Development on 2016-11-18.
 *
 */
@Stateless
public class PaginationController {

    public PaginationResponse getResponse(Integer pageNumber, Integer pageSize, Integer total) {
        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setTotalCount(total);
        paginationResponse.setCurrentPage(pageNumber);
        paginationResponse.setPageSize(pageSize);
        return paginationResponse;
    }

}
