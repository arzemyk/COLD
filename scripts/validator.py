from exception import ValidationException
import config.config

class Validator(object):
    @staticmethod
    def validate(cold_config):
        required_simple_keys = ['coldLocation', 'springConfiguration', 'classPath']
        required_complex_keys = {'main' : {'type': config.config.Mapping, 'inside_keys': ['className', 'host', 'standAlone']},
                                 'beansDistribution': {'type': config.config.Sequence, 'inside_keys': ['host', 'beans']}}

        for key in required_simple_keys:
            if key not in cold_config:
                raise ValidationException("Missing parameter: " + key)

        for key in required_complex_keys:
            if key not in cold_config:
                raise ValidationException("Missing parameter: " + key)
            else:
                key_type = required_complex_keys[key]['type']
                for inside_key in required_complex_keys[key]['inside_keys']:
                    if not isinstance(cold_config[key], key_type):
                        raise ValidationException("Wrong configuration for parameter: " + key)

                    if isinstance(cold_config[key], config.config.Sequence):
                        if len(cold_config[key]) == 0:
                            raise ValidationException("Missing configuration for parameter: " + key)

                        for entry in cold_config[key]:
                            if inside_key not in entry:
                                raise ValidationException("Missing parameter: " + inside_key + " in " + key)
                    else:
                        if inside_key not in cold_config[key]:
                            raise ValidationException("Missing parameter: " + inside_key + " in " + key)
