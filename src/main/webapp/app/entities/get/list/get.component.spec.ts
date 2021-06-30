import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GetService } from '../service/get.service';

import { GetComponent } from './get.component';

describe('Component Tests', () => {
  describe('Get Management Component', () => {
    let comp: GetComponent;
    let fixture: ComponentFixture<GetComponent>;
    let service: GetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GetComponent],
      })
        .overrideTemplate(GetComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GetComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(GetService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.gets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
