jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGet, Get } from '../get.model';
import { GetService } from '../service/get.service';

import { GetRoutingResolveService } from './get-routing-resolve.service';

describe('Service Tests', () => {
  describe('Get routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: GetRoutingResolveService;
    let service: GetService;
    let resultGet: IGet | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(GetRoutingResolveService);
      service = TestBed.inject(GetService);
      resultGet = undefined;
    });

    describe('resolve', () => {
      it('should return IGet returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGet).toEqual({ id: 123 });
      });

      it('should return new IGet if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGet = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultGet).toEqual(new Get());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Get })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGet).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
