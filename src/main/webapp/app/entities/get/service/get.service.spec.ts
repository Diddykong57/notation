import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGet, Get } from '../get.model';

import { GetService } from './get.service';

describe('Service Tests', () => {
  describe('Get Service', () => {
    let service: GetService;
    let httpMock: HttpTestingController;
    let elemDefault: IGet;
    let expectedResult: IGet | IGet[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GetService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        note: 0,
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

      it('should create a Get', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Get()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Get', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            note: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Get', () => {
        const patchObject = Object.assign({}, new Get());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Get', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            note: 1,
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

      it('should delete a Get', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGetToCollectionIfMissing', () => {
        it('should add a Get to an empty array', () => {
          const get: IGet = { id: 123 };
          expectedResult = service.addGetToCollectionIfMissing([], get);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(get);
        });

        it('should not add a Get to an array that contains it', () => {
          const get: IGet = { id: 123 };
          const getCollection: IGet[] = [
            {
              ...get,
            },
            { id: 456 },
          ];
          expectedResult = service.addGetToCollectionIfMissing(getCollection, get);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Get to an array that doesn't contain it", () => {
          const get: IGet = { id: 123 };
          const getCollection: IGet[] = [{ id: 456 }];
          expectedResult = service.addGetToCollectionIfMissing(getCollection, get);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(get);
        });

        it('should add only unique Get to an array', () => {
          const getArray: IGet[] = [{ id: 123 }, { id: 456 }, { id: 38046 }];
          const getCollection: IGet[] = [{ id: 123 }];
          expectedResult = service.addGetToCollectionIfMissing(getCollection, ...getArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const get: IGet = { id: 123 };
          const get2: IGet = { id: 456 };
          expectedResult = service.addGetToCollectionIfMissing([], get, get2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(get);
          expect(expectedResult).toContain(get2);
        });

        it('should accept null and undefined values', () => {
          const get: IGet = { id: 123 };
          expectedResult = service.addGetToCollectionIfMissing([], null, get, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(get);
        });

        it('should return initial array if no Get is added', () => {
          const getCollection: IGet[] = [{ id: 123 }];
          expectedResult = service.addGetToCollectionIfMissing(getCollection, undefined, null);
          expect(expectedResult).toEqual(getCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
