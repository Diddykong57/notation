import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TypeAssessment } from 'app/entities/enumerations/type-assessment.model';
import { IAssessment, Assessment } from '../assessment.model';

import { AssessmentService } from './assessment.service';

describe('Service Tests', () => {
  describe('Assessment Service', () => {
    let service: AssessmentService;
    let httpMock: HttpTestingController;
    let elemDefault: IAssessment;
    let expectedResult: IAssessment | IAssessment[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AssessmentService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        coefCont: 0,
        type: TypeAssessment.CE,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Assessment', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Assessment()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Assessment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            coefCont: 1,
            type: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Assessment', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
          },
          new Assessment()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Assessment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            coefCont: 1,
            type: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Assessment', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAssessmentToCollectionIfMissing', () => {
        it('should add a Assessment to an empty array', () => {
          const assessment: IAssessment = { id: 123 };
          expectedResult = service.addAssessmentToCollectionIfMissing([], assessment);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(assessment);
        });

        it('should not add a Assessment to an array that contains it', () => {
          const assessment: IAssessment = { id: 123 };
          const assessmentCollection: IAssessment[] = [
            {
              ...assessment,
            },
            { id: 456 },
          ];
          expectedResult = service.addAssessmentToCollectionIfMissing(assessmentCollection, assessment);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Assessment to an array that doesn't contain it", () => {
          const assessment: IAssessment = { id: 123 };
          const assessmentCollection: IAssessment[] = [{ id: 456 }];
          expectedResult = service.addAssessmentToCollectionIfMissing(assessmentCollection, assessment);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(assessment);
        });

        it('should add only unique Assessment to an array', () => {
          const assessmentArray: IAssessment[] = [{ id: 123 }, { id: 456 }, { id: 52122 }];
          const assessmentCollection: IAssessment[] = [{ id: 123 }];
          expectedResult = service.addAssessmentToCollectionIfMissing(assessmentCollection, ...assessmentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const assessment: IAssessment = { id: 123 };
          const assessment2: IAssessment = { id: 456 };
          expectedResult = service.addAssessmentToCollectionIfMissing([], assessment, assessment2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(assessment);
          expect(expectedResult).toContain(assessment2);
        });

        it('should accept null and undefined values', () => {
          const assessment: IAssessment = { id: 123 };
          expectedResult = service.addAssessmentToCollectionIfMissing([], null, assessment, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(assessment);
        });

        it('should return initial array if no Assessment is added', () => {
          const assessmentCollection: IAssessment[] = [{ id: 123 }];
          expectedResult = service.addAssessmentToCollectionIfMissing(assessmentCollection, undefined, null);
          expect(expectedResult).toEqual(assessmentCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
