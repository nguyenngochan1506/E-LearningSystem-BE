#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const feature = process.argv[2];
if (!feature) {
    console.error('❌ Vui lòng nhập tên feature. Ví dụ: springgen user');
    process.exit(1);
}

const className = feature.charAt(0).toUpperCase() + feature.slice(1);
const basePath = `src/main/java/vn/edu/hcmuaf/fit/elearning/feature/${feature}`;

const files = [
    {
        filename: `${className}Entity.java`,
        content: `package vn.edu.hcmuaf.fit.elearning.feature.${feature};

import jakarta.persistence.*;

@Entity
public class ${className}Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Add your fields here
}
`
    },
    {
        filename: `${className}Repository.java`,
        content: `package vn.edu.hcmuaf.fit.elearning.feature.${feature};

import org.springframework.data.jpa.repository.JpaRepository;

public interface ${className}Repository extends JpaRepository<${className}Entity, Long> {
}
`
    },
    {
        filename: `${className}Service.java`,
        content: `package vn.edu.hcmuaf.fit.elearning.feature.${feature};

import org.springframework.stereotype.Service;

@Service
public class ${className}Service {
    // Inject repository here
}
`
    },
    {
        filename: `${className}Controller.java`,
        content: `package vn.edu.hcmuaf.fit.elearning.feature.${feature};

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/${feature}")
public class ${className}Controller {
    // Inject service here
}
`
    }
];

// Tạo thư mục gốc và subfolder dto, impl
fs.mkdirSync(path.join(basePath, 'dto'), { recursive: true });
fs.mkdirSync(path.join(basePath, 'impl'), { recursive: true });

// Ghi file
files.forEach(file => {
    const fullPath = path.join(basePath, file.filename);
    fs.writeFileSync(fullPath, file.content);
    console.log(`✅ Created ${fullPath}`);
});
