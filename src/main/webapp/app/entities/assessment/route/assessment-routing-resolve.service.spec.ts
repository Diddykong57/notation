jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAssessment, Assessment } from '../assessment.model';
import { AssessmentService } from '../service/assessment.service';

import { AssessmentRoutingResolveService } from './assessment-routing-resolve.service';

describe('Service Tests', () => {
  describe('Assessment routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AssessmentRoutingResolveService;
    let service: AssessmentService;
    let resultAssessment: IAssessment | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AssessmentRoutingResolveService);
      service = TestBed.inject(AssessmentService);
      resultAssessment = undefined;
    });

    describe('resolve', () => {
      it('should return IAssessment returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAssessment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAssessment).toEqual({ id: 123 });
      });

      it('should return new IAssessment if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAssessment = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAssessment).toEqual(new Assessment());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Assessment })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAssessment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAssessment).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
