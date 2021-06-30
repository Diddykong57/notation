import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDegree, Degree } from '../degree.model';

import { DegreeService } from './degree.service';

describe('Service Tests', () => {
  describe('Degree Service', () => {
    let service: DegreeService;
    let httpMock: HttpTestingController;
    let elemDefault: IDegree;
    let expectedResult: IDegree | IDegree[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DegreeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nameDipl: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Degree', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Degree()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Degree', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nameDipl: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Degree', () => {
        const patchObject = Object.assign(
          {
            nameDipl: 'BBBBBB',
          },
          new Degree()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Degree', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nameDipl: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Degree', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDegreeToCollectionIfMissing', () => {
        it('should add a Degree to an empty array', () => {
          const degree: IDegree = { id: 123 };
          expectedResult = service.addDegreeToCollectionIfMissing([], degree);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(degree);
        });

        it('should not add a Degree to an array that contains it', () => {
          const degree: IDegree = { id: 123 };
          const degreeCollection: IDegree[] = [
            {
              ...degree,
            },
            { id: 456 },
          ];
          expectedResult = service.addDegreeToCollectionIfMissing(degreeCollection, degree);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Degree to an array that doesn't contain it", () => {
          const degree: IDegree = { id: 123 };
          const degreeCollection: IDegree[] = [{ id: 456 }];
          expectedResult = service.addDegreeToCollectionIfMissing(degreeCollection, degree);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(degree);
        });

        it('should add only unique Degree to an array', () => {
          const degreeArray: IDegree[] = [{ id: 123 }, { id: 456 }, { id: 10742 }];
          const degreeCollection: IDegree[] = [{ id: 123 }];
          expectedResult = service.addDegreeToCollectionIfMissing(degreeCollection, ...degreeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const degree: IDegree = { id: 123 };
          const degree2: IDegree = { id: 456 };
          expectedResult = service.addDegreeToCollectionIfMissing([], degree, degree2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(degree);
          expect(expectedResult).toContain(degree2);
        });

        it('should accept null and undefined values', () => {
          const degree: IDegree = { id: 123 };
          expectedResult = service.addDegreeToCollectionIfMissing([], null, degree, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(degree);
        });

        it('should return initial array if no Degree is added', () => {
          const degreeCollection: IDegree[] = [{ id: 123 }];
          expectedResult = service.addDegreeToCollectionIfMissing(degreeCollection, undefined, null);
          expect(expectedResult).toEqual(degreeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
